package com.heyyczer.monopolyfarming.jobs;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.controllers.GameController;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameStarter {

    public static void startRunnable() {
        Bukkit.getAsyncScheduler().runAtFixedRate(Main.getPlugin(), task -> {
            for (Map.Entry<UUID, GameRoom> game : GameController.GAMES.entrySet()) {
                if (game.getValue().getStatus() != GameStatus.STARTING) continue;

                int timeToStart = game.getValue().getTimeToStart();

                GameRoom newGame = game.getValue();
                if (timeToStart <= 0) {
                    newGame.setTimeToStart(0);
                    newGame.setStatus(GameStatus.STARTED);
                    newGame.startGame();
                } else {
                    newGame.setTimeToStart(timeToStart - 1);
                }

                GameController.GAMES.put(game.getKey(), newGame);
            }
        }, 1L, 1L, TimeUnit.SECONDS);
    }

}
