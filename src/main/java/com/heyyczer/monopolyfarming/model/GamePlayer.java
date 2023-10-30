package com.heyyczer.monopolyfarming.model;

import com.heyyczer.monopolyfarming.controller.GameController;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.UUID;

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
		final GameRoom room = GameController.getGames().get(this.gameUUID);
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

					int i = (gamePlayer.getPosition() + 1) % room.getTiles().size();
					if (i < (gamePlayer.getPosition() + 1)) {
						i += 1;
					}

					gamePlayer.setPosition(i);
				} else {
					gamePlayer.getPlayer().setExp(0);
					cancel();

					runnable.run();
				}
			}
		}.runTaskTimer(SimplePlugin.getInstance(), 5L, 5);
//		}.runTaskTimer(SimplePlugin.getInstance(), 20L, 20);
	}

	public void updatePlayer() {
		final GameRoom room = GameController.getGames().get(gameUUID);
		room.getPlayers().stream().filter(gamePlayer -> gamePlayer.getPlayer().getUniqueId() == player.getUniqueId())
				.forEach(gamePlayer -> {
					if (gamePlayer.getPlayer().getUniqueId() == this.getPlayer().getUniqueId()) {
						gamePlayer = this;
						gamePlayer.getPlayer().teleport(room.getTiles().get(gamePlayer.getPosition()).getLocation().clone().add(0.5f, 1.0f, 0.5f));
					}
				});
	}

}
