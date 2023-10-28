package com.heyyczer.monopolyfarming.game.corner;

import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.ICorner;

public class ArrestTile implements ICorner {

	@Override
    public void callback(GamePlayer player) {
		player.setPosition(16);
    }
	
}
