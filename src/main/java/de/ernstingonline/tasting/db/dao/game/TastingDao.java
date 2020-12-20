package de.ernstingonline.tasting.db.dao.game;

import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.db.entities.game.Tasting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TastingDao extends JpaRepository<Tasting, Long> {
    Set<Tasting> findByPlayersIsContaining(Player player);
}
