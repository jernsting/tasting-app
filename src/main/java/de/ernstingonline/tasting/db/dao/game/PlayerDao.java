package de.ernstingonline.tasting.db.dao.game;

import de.ernstingonline.tasting.db.entities.game.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerDao extends JpaRepository<Player, Long> {
    List<Player> findByUsername(String username);
}
