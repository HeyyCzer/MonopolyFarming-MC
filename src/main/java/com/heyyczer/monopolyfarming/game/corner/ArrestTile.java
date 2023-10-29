package com.heyyczer.monopolyfarming.game.corner;

import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.Tile;
import com.heyyczer.monopolyfarming.model.interfaces.ICorner;

public class ArrestTile implements ICorner {

	@Override
    public void onPlayerLand(GamePlayer player, Tile tile, int diceValue) {
		player.setPosition(16);
    }
	
}
