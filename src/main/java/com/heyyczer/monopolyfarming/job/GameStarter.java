package com.heyyczer.monopolyfarming.job;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.helper.TitleHelper;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;

public class GameStarter {

    public static void startRunnable() {
        Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), task -> {
			for (Map.Entry<UUID, GameRoom> game : GameController.GAMES.entrySet()) {
				System.out.println("Game: " + game.getKey() + " - " + game.getValue().getStatus() + " - "
						+ game.getValue().getPlayers().size());
				System.out.println("Players: ");
				for (GamePlayer gp : game.getValue().getPlayers()) {
					System.out.println("NAME: " + gp.getPlayer().getName() + " - $" + gp.getBalance() + " - POS: " + gp.getPosition());
				}
				System.out.println();

                if (game.getValue().getStatus() != GameStatus.STARTING || game.getValue().getPlayers().size() < 2) continue;

				int timeToStart = game.getValue().getTimeToStart();

				game.getValue().getPlayers().forEach(player -> {
					TitleHelper.sendTitle(player.getPlayer(), "§a§lINICIANDO", "O jogo iniciará em §e" + timeToStart + " segundos", 0, 5000, 0);
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
