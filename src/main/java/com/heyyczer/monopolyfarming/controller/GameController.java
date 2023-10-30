package com.heyyczer.monopolyfarming.controller;

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
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameController {

	protected static final Map<Integer, ICorner> Corners = new HashMap<>() {{
        put(1, new StartTile());
        put(16, new PoliceTile());
        put(31, new EventTile());
        put(46, new ArrestTile());
    }};

	protected static final Map<Integer, ISpecial> Specials = new HashMap<>() {{
        put(8, new GuaraniTile());
        put(24, new AviaoTile());
        put(53, new TerraNovaTile());
	}};

	@Getter
	protected static final Map<UUID, GameRoom> Games = new HashMap<>();

	public static UUID createGame() {
		final UUID gameUUID = UUID.randomUUID();

		GameRoom game = new GameRoom(gameUUID);
		game.setStatus(GameStatus.STARTING);
		game.setTimeToStart(10);
		Games.put(gameUUID, game);

		return gameUUID;
	}
	
	public static GameRoom getGameRoomByPlayer(Player player) {
		for (GameRoom gameRoom : Games.values()) {
			for (GamePlayer gamePlayer : gameRoom.getPlayers()) {
				if (gamePlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
					return gameRoom;
				}
			}
		}
		return null;
	}

}
