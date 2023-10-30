package com.heyyczer.monopolyfarming.game.special;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.Tile;
import com.heyyczer.monopolyfarming.model.interfaces.ISpecial;

public class TerraNovaTile implements ISpecial {

    @Override
    public void onPlayerLand(GamePlayer player, Tile tile, int diceValue) {
        GameController.getGames().get(player.getGameUUID()).getTurnController().nextPlayer();
        GameController.getGames().get(player.getGameUUID()).getTurnController().setWaiting(false);
    }

}
