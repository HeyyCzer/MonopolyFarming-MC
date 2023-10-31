package com.heyyczer.monopolyfarming.job;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.helper.NumberHelper;
import com.heyyczer.monopolyfarming.helper.TitleHelper;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Map;
import java.util.UUID;

public class GameStarter {

    public static void startRunnable() {
        Bukkit.getScheduler().runTaskTimer(SimplePlugin.getInstance(), task -> {
			for (Map.Entry<UUID, GameRoom> game : GameController.getGames().entrySet()) {
				if (game.getValue().getStatus() == GameStatus.STARTED) {
                    for (GamePlayer player : game.getValue().getPlayers()) {
                        player.getPlayer().sendActionBar(Component.text("§aSaldo: §b$" + NumberHelper.format(player.getBalance())).asComponent());
                    }
                    continue;
                }

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

                GameController.getGames().put(game.getKey(), newGame);
            }
        }, 20L, 20L);
    }

}
