package com.heyyczer.monopolyfarming.controller;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.model.*;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MapController {

	public static Map<Integer, Tile> prepareMap(UUID uuid) {
		final Map<Integer, Tile> tiles = new HashMap<>();

		// Clone the World
		MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = core.getMVWorldManager();

		final String worldName = "game-" + uuid;
		worldManager.cloneWorld("game", worldName);

		// Set up the tiles in the list
		YamlConfiguration config = Main.getTilesConfig();
		Set<String> configTiles = config.getConfigurationSection("locations").getKeys(false);
		World world = Bukkit.getWorld(worldName);
		for (String tile : configTiles) {
			final int index = Integer.parseInt(tile.split("-")[1]);
			final String configKey = "locations." + tile;

			final Location location = new Location(
					world,
					config.getInt(configKey + ".location.x"),
					config.getInt(configKey + ".location.y"),
					config.getInt(configKey + ".location.z"),
					config.getInt(configKey + ".location.yaw"),
					0
			);

			final TileType type = TileType.valueOf(config.getString(configKey + ".type"));
			final Tile t = new Tile(
					index,
					location,
					config.getString(configKey + ".name"),
					type
			);

			t.setDescription(config.getStringList(configKey + ".description"));
			t.setSchematics(config.getStringList(configKey + ".schematics"));
			t.setPrice(config.getInt(configKey + ".price"));
			t.setRent(config.getInt(configKey + ".rent"));
			t.setSell(config.getInt(configKey + ".sell"));

			switch (type) {
				case CORNER:
					t.setCorner(new Corner(GameController.Corners.get(index)));
					break;
				case PURCHASABLE:
					t.setProperty(new Property());
					break;
				case SPECIAL:
					t.setSpecial(new Special(GameController.Specials.get(index)));
					break;
				default:
					break;
			}

			tiles.put(index, t);
		}
		return tiles;
	}
	
}
