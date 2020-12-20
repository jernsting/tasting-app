package de.ernstingonline.tasting.db.dao.samples;

import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.db.entities.game.Tasting;
import de.ernstingonline.tasting.db.entities.samples.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProductDao extends JpaRepository<Product, Long> {
    Set<Product> findByTasting(Tasting tasting);
    Set<Product> findByPlayer(Player player);
}
