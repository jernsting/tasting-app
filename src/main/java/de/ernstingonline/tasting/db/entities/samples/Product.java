package de.ernstingonline.tasting.db.entities.samples;

import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.db.entities.game.Tasting;

import javax.persistence.*;

@Entity()
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Player player;

    @ManyToOne()
    private Tasting tasting;

    private String name;
    private boolean revealed;
    private Long playOrder;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    public Long getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(Long playOrder) {
        this.playOrder = playOrder;
    }

    @Basic
    public boolean isRevealed() { return revealed; }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Tasting getTasting() {
        return tasting;
    }

    public void setTasting(Tasting tasting) {
        this.tasting = tasting;
    }
}
