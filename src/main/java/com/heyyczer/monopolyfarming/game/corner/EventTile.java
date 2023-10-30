package com.heyyczer.monopolyfarming.game.corner;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.Tile;
import com.heyyczer.monopolyfarming.model.interfaces.ICorner;

public class EventTile implements ICorner {

	@Override
    public void onPlayerLand(GamePlayer player, Tile tile, int diceValue) {
        player.getPlayer().sendMessage("§6§lFEIRA §7Olá! Boas-vindas à feira, você pode apreciar os produtos trazidos por fazendeiros aqui. Todos os anos os fazendeiros trazem os seus melhores produtos para competir. Fique até sua próxima jogada.");

        GameController.getGames().get(player.getGameUUID()).getTurnController().nextPlayer();
        GameController.getGames().get(player.getGameUUID()).getTurnController().setWaiting(false);
    }
	
}
