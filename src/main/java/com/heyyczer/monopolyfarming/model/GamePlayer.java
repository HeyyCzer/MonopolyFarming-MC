package com.heyyczer.monopolyfarming.model;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.controller.GameController;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;

@Getter
@RequiredArgsConstructor
public class GamePlayer {

    @NonNull
    private UUID gameUUID;

    @NonNull
    private Player player;

    @Setter
    private NPC playerNpc;

	private int balance = 1000000;
	
	private int position = 1;

	private boolean arrested = false;

	private int skips = 0;

	public void addBalance(int amount) {
		balance += amount;
		updatePlayer();
	}

	public void subtractBalance(int amount) {
		balance -= amount;
		updatePlayer();
	}

	public void setPosition(int position) {
		this.position = position;
		updatePlayer();
	}

	public void move(int totalTiles, Runnable runnable) {
		final GameRoom room = GameController.GAMES.get(this.gameUUID);
		room.getTurnController().setWaiting(true);
		
		final GamePlayer gamePlayer = this;
		gamePlayer.getPlayer().setLevel(totalTiles);

		new BukkitRunnable() {
			int walked = 0;

			@Override
			public void run() {
				if (walked < totalTiles) {
					walked++;
					gamePlayer.getPlayer().setLevel(totalTiles - walked);
					gamePlayer.setPosition((gamePlayer.getPosition() + 1) % room.getTiles().size());
				} else {
					gamePlayer.getPlayer().setExp(0);
					// room.getTurnController().nextPlayer();
					// room.getTurnController().setWaiting(false);
					cancel();

					runnable.run();
				}
			}
		}.runTaskTimer(Main.getInstance(), 20L, 20);
	}

	public void updatePlayer() {
		final GameRoom room = GameController.GAMES.get(gameUUID);
		room.getPlayers().stream().filter(gamePlayer -> gamePlayer.getPlayer().getUniqueId() == player.getUniqueId())
				.forEach(gamePlayer -> {
					if (gamePlayer.getPlayer().getUniqueId() == this.getPlayer().getUniqueId()) {
						gamePlayer = this;
						gamePlayer.getPlayer().teleport(room.getTiles().get(gamePlayer.getPosition()).getLocation().clone().add(0.5f, 1.0f, 0.5f));
					}
				});
	}

}
