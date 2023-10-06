package com.heyyczer.monopolyfarming.controllers;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.game.specialtiles.AviaoTile;
import com.heyyczer.monopolyfarming.game.specialtiles.GuaraniTile;
import com.heyyczer.monopolyfarming.game.specialtiles.TerraNovaTile;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import com.heyyczer.monopolyfarming.model.IService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class GameController {

    private static Map<String, IService> SERVICES = new HashMap<>() {{
        put("GUARANI", new GuaraniTile());
        put("AVIACAO", new AviaoTile());
        put("TERRA_NOVA", new TerraNovaTile());
    }};

    public static Map<UUID, GameRoom> GAMES = new HashMap<>();

    public static void createGame() {
        final UUID gameUUID = UUID.randomUUID();

        List<Player> players = (List<Player>) Bukkit.getServer().getOnlinePlayers();
        List<GamePlayer> gamePlayers = new ArrayList<>();

        for (Player player : players) {
            gamePlayers.add(new GamePlayer(
                    gameUUID,
                    player
            ));
        }

        GameRoom game = new GameRoom(gamePlayers);
        game.setStatus(GameStatus.STARTING);
        game.setTimeToStart(10);
        GAMES.put(gameUUID, game);
    }

}
