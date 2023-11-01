package com.heyyczer.monopolyfarming.model;

import com.heyyczer.monopolyfarming.controller.MapController;
import com.heyyczer.monopolyfarming.controller.TurnController;
import com.heyyczer.monopolyfarming.helper.TitleHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.*;

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
	private Map<Integer, Tile> tiles = new HashMap<>();

	private TurnController turnController;

	public GameRoom(UUID uuid) {
		this.uuid = uuid;
		this.turnController = new TurnController(uuid);
	}

	public void startGame() {
		Collections.shuffle(this.getPlayers());

		for (GamePlayer player : this.getPlayers()) {
			TitleHelper.sendTitle(player.getPlayer(), "§e§lINICIANDO...", "O mapa está sendo preparado, aguarde...", 0,
					30000, 0);
			player.getPlayer().sendMessage("§e§lINICIANDO... §fO mapa está sendo preparado, aguarde...");
		}

		tiles = MapController.prepareMap(this.getUuid());

		// Get the first tile
		Tile firstTile = tiles.get(1);

		for (Tile tile : tiles.values()) {
			tile.buildHologram(this.getUuid());
		}

		// Teleport the players to the first tile
		for (GamePlayer player : this.getPlayers()) {
			player.getPlayer().teleport(firstTile.getLocation().clone().add(0.5f, 1.0f, 0.5f));
			TitleHelper.sendTitle(player.getPlayer(), "§b§lINICIADO", "Tenha um ótimo jogo :D", 0, 10000, 0);
			player.getPlayer().setGameMode(GameMode.ADVENTURE);
			player.getPlayer().sendMessage("§b§lINICIADO! §fO mapa está pronto, tenha um ótimo jogo :D");
		}

		// Start the turn controller
		Bukkit.getScheduler().runTaskLater(SimplePlugin.getInstance(), () -> {
			this.turnController.nextPlayer();
			this.turnController.setWaiting(false);
		}, 5 * 20L);
	}

	public List<Tile> getPlayerTiles(Player player) {
		return this.getTiles().values().stream().filter(tile -> {
			GamePlayer owner = tile.getOwner();
			if (owner != null) {
				return owner.getPlayer().getUniqueId() == player.getUniqueId();
			}
			return false;
		}).toList();
	}

}
