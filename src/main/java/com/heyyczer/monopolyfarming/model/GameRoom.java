package com.heyyczer.monopolyfarming.model;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<GamePlayer> players;

    @Setter
    private int currentPlayerIndex = 0;

    @Setter
    private GameStatus status = GameStatus.STARTING;

    @Setter
	private int timeToStart = 10;
	
	private static final Map<Integer, Location> tiles = new HashMap<>();

	private TurnController turnController;

	public GameRoom(UUID uuid, ArrayList<GamePlayer> players) {
		this.uuid = uuid;
		this.players = players;
		this.turnController = new TurnController(uuid);
	}

	public void startGame() {
		for (GamePlayer player : this.getPlayers()) {
			TitleHelper.sendTitle(player.getPlayer(), "§e§lINICIANDO...", "O mapa está sendo preparado, aguarde...", 0, 30000, 0);
			player.getPlayer().sendMessage("§e§lINICIANDO... §fO mapa está sendo preparado, aguarde...");
		}

		MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		MVWorldManager worldManager = core.getMVWorldManager();

		final String worldName = "game-" + this.getUuid();
		worldManager.cloneWorld("game", worldName);

		YamlConfiguration config = Main.getTilesConfig();
		Set<String> configTiles = config.getConfigurationSection("locations").getKeys(false);
		World world = Bukkit.getWorld(worldName);
		for (String tile : configTiles) {
			Location location = new Location(
					world,
					config.getInt("locations." + tile + ".location.x"),
					config.getInt("locations." + tile + ".location.y"),
					config.getInt("locations." + tile + ".location.z"));
			tiles.put(Integer.parseInt(tile.split("-")[1]), location);
		}

		Location firstTile = tiles.get(1);
		final Location firstTileLoc = new Location(world, firstTile.getX() + 0.5f, firstTile.getY() + 1.0f, firstTile.getZ() + 0.5f);

		for (Entry<Integer, Location> tile : tiles.entrySet()) {
			final Location tileLoc = new Location(world, tile.getValue().getX() + 0.5f, tile.getValue().getY() + 3.5f, tile.getValue().getZ() + 0.5f);
			
			final List<String> lines = Arrays.asList(
				"&a&lPropriedade (À venda)",
				"&7" + config.getString("locations.tile-" + tile.getKey() + ".name"),
				"&fValor: &b&l$" + config.getInt("locations.tile-" + tile.getKey() + ".price"),
				"&fAluguel: &b&l$" + config.getInt("locations.tile-" + tile.getKey() + ".rent"),
				"#ICON: PLAYER_HEAD (eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=)"
			);

			DHAPI.createHologram(this.getUuid() + "-tile-" + tile.getKey(), tileLoc, lines);
		}

		for (GamePlayer player : this.getPlayers()) {
			player.getPlayer().teleport(firstTileLoc);
			TitleHelper.sendTitle(player.getPlayer(), "§b§lINICIADO", "Tenha um ótimo jogo :D", 0, 10000, 0);
			player.getPlayer().setGameMode(GameMode.ADVENTURE);
			player.getPlayer().sendMessage("§b§lINICIADO! §fO mapa está pronto, tenha um ótimo jogo :D");
		}

		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
			this.turnController.nextPlayer();
		}, 5 * 20);
	}

}
