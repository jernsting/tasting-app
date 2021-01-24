package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.dao.game.PlayerDao;
import de.ernstingonline.tasting.db.dao.samples.ProductDao;
import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.db.entities.samples.Product;
import de.ernstingonline.tasting.helper.StaticFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/codes")
public class QRController {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private PlayerDao playerDao;

    @GetMapping(value = "/product/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCode(@PathVariable("id") String id,
                                                        Principal principal,
                                                        UriComponentsBuilder builder) {
        Optional<Product> optionalProduct = productDao.findById(Long.parseLong(id));
        if (optionalProduct.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Product product = optionalProduct.get();
        ResponseEntity<BufferedImage> response;

        Player player = playerDao.findByUsername(principal.getName()).get(0);
        if (product.getPlayer() != player)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        try {
            /*URI uri = builder
                    .replacePath(null)
                    .replaceQuery(null)
                    .pathSegment("tasting", product.getId().toString(), "verifystep")
                    .build().toUri();*/
            String uri = "https://tasting.ernsting-online.de/tasting/"+ product.getId().toString() + "/verifystep";
            response = new ResponseEntity<>(StaticFunctions.generateQRCodeImage(uri),
                    HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
        return response;
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

}
