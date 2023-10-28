package com.heyyczer.monopolyfarming.model;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Tile {

	private int index;

	private Location location;

	private String name;

	private TileType type = TileType.PURCHASABLE;

	@Setter
	private Property property;

	@Setter
	private Special special;

	@Setter
	private Corner corner;

	public Tile(int index, Location location, String name, TileType type) {
		this.index = index;
		this.location = location;
		this.name = name;
		this.type = type;
	}

}
