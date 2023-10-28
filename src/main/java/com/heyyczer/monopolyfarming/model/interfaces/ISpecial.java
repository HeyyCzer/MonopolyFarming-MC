package com.heyyczer.monopolyfarming.model.interfaces;

import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.Tile;

public interface ISpecial {

    void onPlayerLand(GamePlayer player, Tile tile, int diceValue);

}
