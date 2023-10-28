package com.heyyczer.monopolyfarming.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.controller.TurnController;
import com.heyyczer.monopolyfarming.helper.TitleHelper;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import eu.decentsoftware.holograms.api.DHAPI;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameRoom {

    private UUID uuid;

	@Setter
	private ArrayList<GamePlayer> players = new ArrayList<>();
	
    @Setter
    private int currentPlayerIndex = 0;

    @Setter
    private GameStatus status = GameStatus.STARTING;

    @Setter
	private int timeToStart = 10;
	
	@Getter
	private final Map<Integer, Tile> tiles = new HashMap<>();

	private TurnController turnController;

	public GameRoom(UUID uuid) {
		this.uuid = uuid;
		this.turnController = new TurnController(uuid);
	}

	public void startGame() {
		for (GamePlayer player : this.getPlayers()) {
			TitleHelper.sendTitle(player.getPlayer(), "§e§lINICIANDO...", "O mapa está sendo preparado, aguarde...", 0,
					30000, 0);
			player.getPlayer().sendMessage("§e§lINICIANDO... §fO mapa está sendo preparado, aguarde...");
		}

		// Clone the World
		MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = core.getMVWorldManager();

		final String worldName = "game-" + this.getUuid();
		worldManager.cloneWorld("game", worldName);

		// Set up the tiles in the list
		YamlConfiguration config = Main.getTilesConfig();
		Set<String> configTiles = config.getConfigurationSection("locations").getKeys(false);
		World world = Bukkit.getWorld(worldName);
		for (String tile : configTiles) {
			final int index = Integer.parseInt(tile.split("-")[1]);
			final Location location = new Location(
					world,
					config.getInt("locations." + tile + ".location.x"),
					config.getInt("locations." + tile + ".location.y"),
					config.getInt("locations." + tile + ".location.z"),
					config.getInt("locations." + tile + ".location.yaw"),
					0
			);

			final TileType type = TileType.valueOf(config.getString("locations." + tile + ".type"));
			final Tile t = new Tile(
					index,
					location,
					config.getString("locations." + tile + ".name"),
					type
			);

			final int price = config.getInt("locations." + tile + ".price");

			switch (type) {
				case CORNER:
					t.setCorner(new Corner(t.getName(), config.getStringList("locations." + tile + ".description"), GameController.CORNERS.get(index)));
					break;
				case PURCHASABLE:
					t.setProperty(new Property(t.getName(), price, config.getInt("locations." + tile + ".rent")));
					break;
				case SPECIAL:
					t.setSpecial(new Special(t.getName(), config.getStringList("locations." + tile + ".description"), price, GameController.SPECIALS.get(index)));
					break;
			}

			tiles.put(index, t);
		}

		// Get the first tile
		Tile firstTile = tiles.get(1);
		final Location firstTileLoc = new Location(world, firstTile.getLocation().getX() + 0.5f,
				firstTile.getLocation().getY() + 1.0f, firstTile.getLocation().getZ() + 0.5f);

		// Create the holograms
		for (Entry<Integer, Tile> tile : tiles.entrySet()) {
			final Location tileLoc = new Location(world, tile.getValue().getLocation().getX() + 0.5f,
					tile.getValue().getLocation().getY() + 3.5f, tile.getValue().getLocation().getZ() + 0.5f);

			final List<String> lines = new ArrayList<>();
			
			switch (tile.getValue().getType()) {
				case CORNER:
					lines.add("&e&l" + tile.getValue().getName());
					lines.addAll(tile.getValue().getCorner().getDescription());
					break;
				case PURCHASABLE:
					lines.add("&a&lPropriedade (À venda)");
					lines.add("&7" + tile.getValue().getName());
					lines.add("&fProprietário: &8Ninguém");
					lines.add("&fPreço: &a$" + tile.getValue().getProperty().getPrice());
					lines.add("&fAluguel: &a$" + tile.getValue().getProperty().getRent());
					lines.add("#ICON: PLAYER_HEAD (eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=)");
					break;
				case SPECIAL:
					lines.add("&e" + tile.getValue().getName());
					lines.addAll(tile.getValue().getSpecial().getDescription());
					break;
			}

			DHAPI.createHologram(this.getUuid() + "-tile-" + tile.getKey(), tileLoc, lines);
		}

		// Teleport the players to the first tile
		for (GamePlayer player : this.getPlayers()) {
			player.getPlayer().teleport(firstTileLoc);
			TitleHelper.sendTitle(player.getPlayer(), "§b§lINICIADO", "Tenha um ótimo jogo :D", 0, 10000, 0);
			player.getPlayer().setGameMode(GameMode.ADVENTURE);
			player.getPlayer().sendMessage("§b§lINICIADO! §fO mapa está pronto, tenha um ótimo jogo :D");
		}

		// Start the turn controller
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> this.turnController.nextPlayer(), 5 * 20L);
	}

}
