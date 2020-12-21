package de.ernstingonline.tasting.db.entities.game;

import de.ernstingonline.tasting.db.entities.samples.Product;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasting")
public class Tasting {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "tastings")
    private Set<Player> players = new HashSet<>();

    @OneToMany()
    private Set<Product> products = new HashSet<>();

    @ManyToOne()
    private Player host;

    private String title;
    private Date date;
    private String inviteCode;
    private boolean opened = true;
    private boolean cancelled = false;
    private boolean started = false;

    /**
     * Add a product to the current tasting
     * @param product product to add
     * @throws IllegalArgumentException If the product is already assigned to a different user
     */
    public void addProduct(Product product) throws IllegalArgumentException {
        if (product.getTasting() != null)
            throw new IllegalArgumentException("Product already taken");
        product.setTasting(this);
        this.products.add(product);
    }

    @Basic
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Basic
    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    @Basic
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Basic
    public String getInviteCode() { return inviteCode; }

    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }

    @Basic
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }
}
