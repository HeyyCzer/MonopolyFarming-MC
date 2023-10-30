package com.heyyczer.monopolyfarming.game.corner;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.Tile;
import com.heyyczer.monopolyfarming.model.interfaces.ICorner;
import org.bukkit.Location;

public class ArrestTile implements ICorner {

	@Override
    public void onPlayerLand(GamePlayer player, Tile tile, int diceValue) {
		player.setPosition(16);
        player.setArrested(true);
        player.setSkips(3);

        GameController.getGames().get(player.getGameUUID()).getTurnController().nextPlayer();
        GameController.getGames().get(player.getGameUUID()).getTurnController().setWaiting(false);

        player.getPlayer().teleport(new Location(player.getPlayer().getWorld(), -101.5, 67, 101.5, -128.5f, 0.0f));
    }
	
}
