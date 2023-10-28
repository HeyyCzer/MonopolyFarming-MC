package com.heyyczer.monopolyfarming.model;

import java.util.UUID;

import org.bukkit.entity.Player;

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

    @Getter
	private int balance = 1000000;
	
	@Getter
	private int position = 1;

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

	public void updatePlayer() {
		GameRoom room = GameController.GAMES.get(gameUUID);
		room.getPlayers().stream().filter(gamePlayer -> gamePlayer.getPlayer().getUniqueId() == player.getUniqueId())
				.forEach(gamePlayer -> {
					if (gamePlayer.getPlayer().getUniqueId() == this.getPlayer().getUniqueId()) {
						gamePlayer = this;
						gamePlayer.getPlayer().teleport(room.getTiles().get(gamePlayer.getPosition()).getLocation().clone().add(0.5f, 1.0f, 0.5f));
					}
				});
		// room.setPlayers(room.getPlayers());
		// GameController.GAMES.put(gameUUID, room);
	}

}
