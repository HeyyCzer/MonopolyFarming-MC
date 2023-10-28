package com.heyyczer.monopolyfarming.jobs;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.controllers.GameController;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;

public class GameStarter {

    public static void startRunnable() {
        Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), task -> {
			for (Map.Entry<UUID, GameRoom> game : GameController.GAMES.entrySet()) {
				System.out.println("Game: " + game.getKey() + " - " + game.getValue().getStatus() + " - " + game.getValue().getPlayers().size());

                if (game.getValue().getStatus() != GameStatus.STARTING || game.getValue().getPlayers().size() < 2) continue;

				int timeToStart = game.getValue().getTimeToStart();

				game.getValue().getPlayers().forEach(player -> {
					player.getPlayer().sendTitle("§a§lINICIANDO", "O jogo iniciará em §e" + timeToStart + " segundos", 0, 20, 0);
				});

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
        }, 20L, 20L);
    }

}
