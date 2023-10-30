package com.heyyczer.monopolyfarming.game.corner;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.Tile;
import com.heyyczer.monopolyfarming.model.interfaces.ICorner;

import java.util.List;

public class PoliceTile implements ICorner {

	@Override
    public void onPlayerLand(GamePlayer player, Tile tile, int diceValue) {
        GameRoom game = GameController.getGames().get(player.getGameUUID());
        if (game == null) return;

        List<String> arrestedPlayers = game.getPlayers().stream()
                .filter(GamePlayer::isArrested).map(p -> p.getPlayer().getName()).toList();

        if (arrestedPlayers.isEmpty()) {
            player.getPlayer().sendMessage("§6§lPOLÍCIA §7Boas-vindas à Delegacia. Infelizmente no momento não há ninguém para ser visitado.");
        } else {
            player.getPlayer().sendMessage("§6§lPOLÍCIA §7Boas-vindas à Delegacia. Você pode visitar o(s) seguinte(s) jogador(es):");
            player.getPlayer().sendMessage("§6§l* §e" + String.join("\n§6§l* §e", arrestedPlayers));
        }

        GameController.getGames().get(player.getGameUUID()).getTurnController().nextPlayer();
        GameController.getGames().get(player.getGameUUID()).getTurnController().setWaiting(false);
    }
	
}
