package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.dao.game.PlayerDao;
import de.ernstingonline.tasting.db.dao.game.TastingDao;
import de.ernstingonline.tasting.db.dao.samples.ProductDao;
import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.db.entities.game.Tasting;
import de.ernstingonline.tasting.db.entities.game.TastingState;
import de.ernstingonline.tasting.db.entities.samples.Product;
import de.ernstingonline.tasting.exceptions.ProductNotFoundException;
import de.ernstingonline.tasting.exceptions.TastingNotFoundException;
import de.ernstingonline.tasting.helper.StaticFunctions;
import de.ernstingonline.tasting.validators.InviteValidator;
import de.ernstingonline.tasting.validators.ProductValidator;
import de.ernstingonline.tasting.validators.TastingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/tasting")
public class TastingController {

    @Autowired
    private TastingDao tastingDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ProductDao productDao;

    @RequestMapping(value = "/invite", method = RequestMethod.GET)
    public String processInvite(Model model) {
        model.addAttribute("invite", new InviteValidator());
        return "tasting/tastingInvite";
    }

    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public String acceptInvite(@Valid @ModelAttribute("invite") InviteValidator invite,
                               BindingResult result,
                               RedirectAttributes atts,
                               Principal principal) {
        if (result.hasErrors())
            return "tasting/tastingInvite";

        String[] parts = invite.getInvite().split("\\$");
        String error_msg = "Ungültiger Einladungscode";

        Optional<Tasting> optionalTasting;
        try {
             optionalTasting = tastingDao.findById(Long.parseLong(parts[0]));
        } catch (Exception e) {
            atts.addFlashAttribute("messages", error_msg);
            return "redirect:/tasting/invite";
        }
        if (!optionalTasting.isPresent()) {
            atts.addFlashAttribute("messages", error_msg);
            return "redirect:/tasting/invite";
        }

        Tasting tasting = optionalTasting.get();
        if (!tasting.getInviteCode().equals(parts[1])) {
            atts.addFlashAttribute("messages", error_msg);
            return "redirect:/tasting/invite";
        }

        if (!tasting.getState().acceptingInvites()) {
            atts.addFlashAttribute("messages", error_msg);
            return "redirect:/tasting/invite";
        }

        Player player = playerDao.findByUsername(principal.getName()).get(0);
        player.addTasting(tasting);

        tastingDao.save(tasting);
        playerDao.save(player);

        return "redirect:/tasting/"+parts[0]+"/view";
    }

    @RequestMapping(value = "{tasting_id}/leave")
    public String leaveTasting(@PathVariable("tasting_id") String tasting_id,
                               Principal principal,
                               RedirectAttributes atts) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        Optional<Tasting> optionalTasting = tastingDao.findById(Long.parseLong(tasting_id));
        if (optionalTasting.isPresent()) {
            Tasting tasting = optionalTasting.get();
            if (tasting.getHost() == player) {
                atts.addFlashAttribute("messages", "Als Host können Sie das Tasting nicht verlassen.");
                return "redirect:/tasting/"+tasting_id+"/view";
            }
            Set<Player> players = tasting.getPlayers();
            players.remove(player);
            tasting.setPlayers(players);
            Set<Tasting> tastings = player.getTastings();
            tastings.remove(tasting);
            player.setTastings(tastings);

            playerDao.save(player);
            tastingDao.save(tasting);
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/{tasting_id}/close")
    public String closeTasting(@PathVariable("tasting_id") String tasting_id,
                               Principal principal) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        Optional<Tasting> optionalTasting = tastingDao.findById(Long.parseLong(tasting_id));
        if (optionalTasting.isPresent()) {
            Tasting tasting = optionalTasting.get();
            if (tasting.getHost() == player) {
                //tasting.setOpened(!tasting.isOpened());
                if (tasting.getState().planned())
                    tasting.setState(TastingState.CLOSED);
                tastingDao.save(tasting);
                List<Product> products = new ArrayList<>(tasting.getProducts());
                Collections.shuffle(products);
                for (int i=0; i<products.size(); i++) {
                    Product product = products.get(i);
                    product.setPlayOrder((long)i);
                    productDao.save(product);
                }
            }
        }
        return "redirect:/tasting/"+tasting_id+"/view";
    }

    @RequestMapping(value = "/{tasting_id}/cancel")
    public String cancelTasting(@PathVariable("tasting_id") String tasting_id,
                               Principal principal) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        Optional<Tasting> optionalTasting = tastingDao.findById(Long.parseLong(tasting_id));
        if (optionalTasting.isPresent()) {
            Tasting tasting = optionalTasting.get();
            if (tasting.getHost() == player) {
                //tasting.setCancelled(true);
                tasting.setState(TastingState.CANCELLED);
                tastingDao.save(tasting);
            }
        }
        return "redirect:/tasting/"+tasting_id+"/view";
    }

    @RequestMapping(value = "/{tasting_id}/start")
    public String startTasting(@PathVariable("tasting_id") String tasting_id,
                               Principal principal,
                               RedirectAttributes atts) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        Optional<Tasting> optionalTasting = tastingDao.findById(Long.parseLong(tasting_id));
        if (optionalTasting.isPresent()) {
            Tasting tasting = optionalTasting.get();
            if (tasting.getHost() == player && tasting.getState().closed()) {
                //tasting.setStarted(true);
                List<Player> blockingPlayers = new ArrayList<>();
                for (Player coplayer : tasting.getPlayers()) {
                    boolean blocking = false;
                    for (Tasting coplayergame : coplayer.getTastings())
                        if (coplayergame.getState().started()) {
                            blocking = true;
                            break;
                        }
                    if (blocking)
                        blockingPlayers.add(coplayer);
                }
                if (blockingPlayers.isEmpty()) {
                    tasting.setState(TastingState.STARTED);
                    tastingDao.save(tasting);
                } else {
                    atts.addFlashAttribute("messages", "Einige Teilnehmer nehmen aktuell an gestarteten Tastings teil, daher kann dieses Tasting nicht gestartet werden.");
                }
            } else if (tasting.getHost() == player && !tasting.getState().closed()) {
                atts.addFlashAttribute("messages", "Tasting muss vor dem Start geschlossen werden");
            }
        }
        return "redirect:/tasting/"+tasting_id+"/view";
    }

    @RequestMapping(value = "/{product_id}/verifystep")
    public String verifyProduct(@PathVariable("product_id") String product_id,
                                Model model,
                                Principal principal) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        Tasting activeTasting = null;
        for (Tasting tasting : player.getTastings())
            if (tasting.getState().started()) {
                activeTasting = tasting;
                break;
            }
        if (activeTasting == null)
            throw new  TastingNotFoundException();

        Optional<Product> optionalProduct = productDao.findById(Long.parseLong(product_id));
        if (!optionalProduct.isPresent())
            throw new ProductNotFoundException();

        boolean right_product = false;
        if (activeTasting.getStep() == optionalProduct.get().getPlayOrder())
            right_product = true;

        model.addAttribute("right_product", right_product);
        return "tasting/verifyStep";
    }

    @RequestMapping(value = "/{tasting_id}/reveal/{product_id}")
    public String revealProduct(@PathVariable("tasting_id") String tasting_id,
                                @PathVariable("product_id") String product_id,
                                Principal principal) {

        Optional<Tasting> optionalTasting = tastingDao.findById(Long.parseLong(tasting_id));
        if (optionalTasting.isPresent()) {
            Tasting tasting = optionalTasting.get();
            Player player = playerDao.findByUsername(principal.getName()).get(0);
            if (tasting.getPlayers().contains(player) && tasting.getState().started()) {
                Optional<Product> optionalProduct = productDao.findById(Long.parseLong(product_id));
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    if (tasting.getHost() == player && tasting.getStep() == product.getPlayOrder()) {
                        product.setRevealed(!product.isRevealed());
                        productDao.save(product);
                        tasting.setStep(tasting.getStep() + 1);
                        tastingDao.save(tasting);
                    }
                }
            }
        }

        return "redirect:/tasting/"+tasting_id+"/view";
    }

    @RequestMapping(value = "/{tasting_id}/delete/{product_id}")
    public String deleteProduct(@PathVariable("tasting_id") String tasting_id,
                                @PathVariable("product_id") String product_id,
                                Principal principal) {

        Optional<Tasting> optionalTasting = tastingDao.findById(Long.parseLong(tasting_id));
        if (optionalTasting.isPresent()) {
            Tasting tasting = optionalTasting.get();
            Player player = playerDao.findByUsername(principal.getName()).get(0);
            if (tasting.getPlayers().contains(player)
                    && !tasting.getState().cancelled()
                    && !tasting.getState().started()) {
                Optional<Product> optionalProduct = productDao.findById(Long.parseLong(product_id));
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    if (product.getPlayer() == player) {

                        player.getProducts().remove(product);
                        tasting.getProducts().remove(product);
                        playerDao.save(player);
                        tastingDao.save(tasting);
                        productDao.delete(product);

                    }
                }
            }
        }

        return "redirect:/tasting/"+tasting_id+"/view";
    }

    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
    public String viewTasting(@PathVariable("id") String id,
                              Principal principal,
                              Model model) {
        Tasting tasting = saveGetTasting(principal, id);

        model.addAttribute("tasting", tasting);
        model.addAttribute("username", principal.getName());
        model.addAttribute("product", new ProductValidator());
        return "tasting/viewTasting";
    }

    @RequestMapping(value = "/{id}/view", method = RequestMethod.POST)
    public String addProduct(@PathVariable("id") String id,
                             Principal principal,
                             @Valid @ModelAttribute("product") ProductValidator product,
                             RedirectAttributes atts,
                             BindingResult result,
                             Model model) {
        Tasting tasting = saveGetTasting(principal, id);
        Player player = playerDao.findByUsername(principal.getName()).get(0);

        model.addAttribute("username", principal.getName());
        model.addAttribute("tasting", tasting);

        if (result.hasErrors())
            return "tasting/viewTasting";
        if (!tasting.getState().planned()) {
            atts.addFlashAttribute("messages", "Tasting ist abgesagt");
            return "redirect:/tasting/"+id+"/view";
        }

        Product dbProduct = new Product();
        dbProduct.setName(product.getName());

        player.addProduct(dbProduct);
        tasting.addProduct(dbProduct);

        productDao.save(dbProduct);
        tastingDao.save(tasting);
        playerDao.save(player);

        return "redirect:/tasting/"+id+"/view";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newTasting(Model model) {
        model.addAttribute("tasting", new TastingValidator());
        return "tasting/newTasting";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String saveTasting(@Valid @ModelAttribute("tasting") TastingValidator tasting,
                           BindingResult result,
                           RedirectAttributes atts,
                           Model model,
                           Principal principal) {
        if (result.hasErrors())
            return "tasting/newTasting";

        Player host = playerDao.findByUsername(principal.getName()).get(0);

        if (host.getCredit() <= 0 && !host.isAdmin()) {
            atts.addFlashAttribute("messages", "Kein Guthaben");
            return "redirect:/";
        }

        if (!host.isAdmin())
            host.setCredit(host.getCredit() - 1);

        Tasting dbTasting = new Tasting();
        Date date1;
        try {
             date1 = new SimpleDateFormat("dd.MM.yyyy").parse(tasting.getDate());
        } catch (ParseException e) {
            try {
                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(tasting.getDate());
            } catch (ParseException f) {
                try {
                    date1 = new SimpleDateFormat("dd-MM-yyyy").parse(tasting.getDate());
                } catch (ParseException g) {
                    throw new IllegalArgumentException("No valid date format");
                }
            }
        }
        dbTasting.setDate(date1);
        dbTasting.setTitle(tasting.getName());
        dbTasting.setInviteCode(StaticFunctions.getString(10));
        host.addHostedTasting(dbTasting);

        tastingDao.save(dbTasting);
        playerDao.save(host);

        return "redirect:/tasting/"+dbTasting.getId()+"/view";
    }

    @RequestMapping("/list")
    public String tastingList(Principal principal, Model model) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);

        Set<Tasting> tastings = tastingDao.findByPlayersIsContaining(player);
        model.addAttribute("tastings", tastings);
        return "tasting/tastingList";
    }


    // HELPER FUNCTIONS
    private Tasting saveGetTasting(Principal principal, String id) {
        Player user = playerDao.findByUsername(principal.getName()).get(0);
        Optional<Tasting> optionalTasting = tastingDao.findById(Long.parseLong(id));
        if (!optionalTasting.isPresent())
            throw new TastingNotFoundException();
        Tasting tasting = optionalTasting.get();

        if (!tasting.getPlayers().contains(user))
            throw new TastingNotFoundException();

        return tasting;
    }
}
