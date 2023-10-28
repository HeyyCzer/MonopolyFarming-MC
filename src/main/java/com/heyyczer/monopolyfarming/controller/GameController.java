package com.heyyczer.monopolyfarming.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.heyyczer.monopolyfarming.game.corner.ArrestTile;
import com.heyyczer.monopolyfarming.game.corner.EventTile;
import com.heyyczer.monopolyfarming.game.corner.PoliceTile;
import com.heyyczer.monopolyfarming.game.corner.StartTile;
import com.heyyczer.monopolyfarming.game.special.AviaoTile;
import com.heyyczer.monopolyfarming.game.special.GuaraniTile;
import com.heyyczer.monopolyfarming.game.special.TerraNovaTile;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import com.heyyczer.monopolyfarming.model.interfaces.ICorner;
import com.heyyczer.monopolyfarming.model.interfaces.ISpecial;

public class GameController {

	public static Map<Integer, ICorner> CORNERS = new HashMap<>() {{
        put(1, new StartTile());
        put(16, new PoliceTile());
        put(31, new EventTile());
        put(46, new ArrestTile());
    }};

    public static Map<Integer, ISpecial> SPECIALS = new HashMap<>() {{
        put(8, new GuaraniTile());
        put(24, new AviaoTile());
        put(53, new TerraNovaTile());
	}};
	
    public static Map<UUID, GameRoom> GAMES = new HashMap<>();

	public static UUID createGame() {
		final UUID gameUUID = UUID.randomUUID();

		GameRoom game = new GameRoom(gameUUID);
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
