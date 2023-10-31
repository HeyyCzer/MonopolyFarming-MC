package com.heyyczer.monopolyfarming.event;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		World world = Bukkit.getWorld("lobby");
		event.getPlayer().teleport(new Location(world, 0.5, 67.5, 0.5, -180.0f, 0.0f));

		UUID targetGameUUID;
		if (GameController.getGames().isEmpty() ||
				GameController.getGames().values().stream().noneMatch(game -> game.getStatus() == GameStatus.STARTING)) {
			targetGameUUID = GameController.createGame();
		} else {
			targetGameUUID = GameController.getGames().values().stream()
					.filter(game -> game.getStatus() == GameStatus.STARTING)
					.findFirst()
					.get()
					.getUuid();
		}

		GameRoom room = GameController.getGames().get(targetGameUUID);
		ArrayList<GamePlayer> players = GameController.getGames().get(targetGameUUID).getPlayers();
		players.add(new GamePlayer(targetGameUUID, event.getPlayer()));
		room.setPlayers(players);
    }

}
