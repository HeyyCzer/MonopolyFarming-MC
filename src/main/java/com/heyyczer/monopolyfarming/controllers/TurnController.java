package com.heyyczer.monopolyfarming.controllers;

import com.heyyczer.monopolyfarming.model.GamePlayer;
import org.bukkit.entity.Player;

public class TurnController {

    public static int CURRENT_PLAYER_INDEX = 0;

    public static void nextPlayer() {
        CURRENT_PLAYER_INDEX = (CURRENT_PLAYER_INDEX + 1) % GameController.GAME_PLAYERS.size();

        final GamePlayer gamePlayer = GameController.GAME_PLAYERS.get(CURRENT_PLAYER_INDEX);
        final Player player = gamePlayer.getPlayer();

        player.sendTitle(
                "§a§lSUA VEZ!",
                "§fDigite §7/dados §fpara se movimentar",
                0, 100, 0
        );
    }

}
