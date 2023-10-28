package com.heyyczer.monopolyfarming.events;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.heyyczer.monopolyfarming.controllers.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;

public class JoinListener implements Listener {

    @EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		UUID targetGameUUID = null;
		if (GameController.GAMES.isEmpty() || 
				GameController.GAMES.values().stream().noneMatch(game -> game.getStatus() == GameStatus.STARTING)) {
			targetGameUUID = GameController.createGame();
		} else {
			targetGameUUID = GameController.GAMES.values().stream()
					.filter(game -> game.getStatus() == GameStatus.STARTING)
					.findFirst()
					.get()
					.getUuid();
		}

		GameRoom room = GameController.GAMES.get(targetGameUUID);
		ArrayList<GamePlayer> players = GameController.GAMES.get(targetGameUUID).getPlayers();
		players.add(new GamePlayer(targetGameUUID, event.getPlayer()));
		room.setPlayers(players);
    }

}
