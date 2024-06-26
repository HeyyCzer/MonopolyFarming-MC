package com.heyyczer.monopolyfarming.event;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class ExitListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// Remove player from any game
		GameRoom gameRoom = GameController.getGameRoomByPlayer(event.getPlayer());
		if (gameRoom == null) return;

		ArrayList<GamePlayer> players = gameRoom.getPlayers().stream()
				.filter(gamePlayer -> gamePlayer.getPlayer().getUniqueId() != event.getPlayer().getUniqueId())
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		if (players.size() < 2) {
			players.forEach(player -> player.getPlayer().kick(Component.text("§cO jogo foi cancelado devido a falta de jogadores!")));
			GameController.getGames().remove(gameRoom.getUuid());
			
			MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
			MVWorldManager worldManager = core.getMVWorldManager();

			final String worldName = "game-" + gameRoom.getUuid();
			worldManager.deleteWorld(worldName);
		} else {
			gameRoom.setPlayers(players);
			GameController.getGames().put(gameRoom.getUuid(), gameRoom);
		}
	}

}
