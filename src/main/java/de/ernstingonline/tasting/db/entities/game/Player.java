package de.ernstingonline.tasting.db.entities.game;

import de.ernstingonline.tasting.db.entities.samples.Product;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "player")
public class Player implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany()
    private Set<Tasting> tastings = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<Product> products = new HashSet<>();

    // Todo: password etc
    private String name;
    private Integer credit = 0;
    private String password;
    private String username;
    private Boolean locked = true;
    private boolean admin = false;
    private Date creationDate = new Date();

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Add a product to the current user
     * @param product product to add
     * @throws IllegalArgumentException If the product is already assigned to a different user
     */
    public void addProduct(Product product) throws IllegalArgumentException {
        if (product.getPlayer() != null)
            throw new IllegalArgumentException("Product already taken");
        product.setPlayer(this);
        this.products.add(product);
    }

    /**
     * Add a tasting to the current user
     *
     * Aka: The user attends to a tasting
     * @param tasting the tasting to add
     */
    public void addTasting(Tasting tasting) {
        this.tastings.add(tasting);
        if (!tasting.getPlayers().contains(this)) {
            Set<Player> players = tasting.getPlayers();
            players.add(this);
            tasting.setPlayers(players);
        }
    }

    /**
     * Get the remaining credits of the user
     * Currently one credit is worth one tasting
     * @return The current credit balance
     */
    @Basic
    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    /**
     * The name that is shown when the user attends to a tasting
     *
     * The user can easily change this name (aka displayname)
     * @return
     */
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * This ID is only generated for database purposes
     * @return
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return a list of all products that this user has got
     * @return List of products
     */
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Basic
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Get a list of tastings, that a user has hosted
     * @return
     */
    public Set<Tasting> getTastings() {
        return tastings;
    }

    public void setTastings(Set<Tasting> tastings) {
        this.tastings = tastings;
    }

    /**
     * Get the assigned roles of the user
     * @return Currently only the user role is assigned
     */
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_USER");
        grantedAuthorities.add(auth);

        if (this.isAdmin()) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            grantedAuthorities.add(authority);
        }


        return grantedAuthorities;
    }

    /**
     * Login password
     * @return password
     */
    @Override
    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Login username
     *
     * could be changed by the user
     * @return username
     */
    @Override
    @Basic
    @Column(nullable = false, unique = true, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Currently accounts do not expire
     * @return true
     */
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * If the account is locked
     * @return true if the account is locked, else false
     */
    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Basic
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Currently credentials do not expire
     * @return true
     */
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Currently accounts do not get disabled
     * @return true
     */
    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
}
