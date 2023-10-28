package com.heyyczer.monopolyfarming.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.heyyczer.monopolyfarming.game.specialtiles.AviaoTile;
import com.heyyczer.monopolyfarming.game.specialtiles.GuaraniTile;
import com.heyyczer.monopolyfarming.game.specialtiles.TerraNovaTile;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import com.heyyczer.monopolyfarming.model.IService;

public class GameController {

    public static Map<String, IService> SERVICES = new HashMap<>() {{
        put("GUARANI", new GuaraniTile());
        put("AVIACAO", new AviaoTile());
        put("TERRA_NOVA", new TerraNovaTile());
    }};

    public static Map<UUID, GameRoom> GAMES = new HashMap<>();

	public static UUID createGame() {
		final UUID gameUUID = UUID.randomUUID();

		GameRoom game = new GameRoom(gameUUID, new ArrayList<>());
		game.setStatus(GameStatus.STARTING);
		game.setTimeToStart(10);
		// game.setTimeToStart(60);
		GAMES.put(gameUUID, game);

		return gameUUID;
	}
	
	public static GameRoom getGameRoomByPlayer(Player player) {
		for (GameRoom gameRoom : GAMES.values()) {
			for (GamePlayer gamePlayer : gameRoom.getPlayers()) {
				if (gamePlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
					return gameRoom;
				}
			}
		}
		return null;
	}

}
