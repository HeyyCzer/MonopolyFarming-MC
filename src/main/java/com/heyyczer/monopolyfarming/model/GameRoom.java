package com.heyyczer.monopolyfarming.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.controller.MapController;
import com.heyyczer.monopolyfarming.controller.TurnController;
import com.heyyczer.monopolyfarming.helper.TitleHelper;

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
	private Map<Integer, Tile> tiles = new HashMap<>();

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
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> this.turnController.nextPlayer(), 5 * 20L);
	}

}
