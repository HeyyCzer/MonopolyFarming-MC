package com.heyyczer.monopolyfarming.controller;

import com.heyyczer.monopolyfarming.helper.TitleHelper;
import com.heyyczer.monopolyfarming.menu.ArrestMenu;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TurnController {

	@Getter
	private UUID uuid;

	public TurnController(UUID uuid) {
		this.uuid = uuid;
	}

	@Getter @Setter
    private boolean waiting = true;

    public int CURRENT_PLAYER_INDEX = 0;

	public void nextPlayer() {
		final GameRoom gameRoom = GameController.Games.get(this.getUuid());

        CURRENT_PLAYER_INDEX = (CURRENT_PLAYER_INDEX + 1) % gameRoom.getPlayers().size();

        final GamePlayer gamePlayer = gameRoom.getPlayers().get(CURRENT_PLAYER_INDEX);
		final Player player = gamePlayer.getPlayer();

		gameRoom.getPlayers().forEach(p -> {
			if (p.getPlayer().getUniqueId() != player.getUniqueId()) {
				TitleHelper.sendTitle(p.getPlayer(), "§c§lOPONENTE", "§fAguarde enquanto §e" + player.getName() + " §frealiza a jogada", 0, 5000, 0);

				p.getPlayer()
						.sendMessage("§c§lOPONENTE §fAguarde enquanto §e" + player.getName() + " §frealiza a jogada");
			}
		});

		if (!gamePlayer.isArrested()) {
			if (gamePlayer.getSkips() > 0) {
				this.nextPlayer();
				return;
			}

			TitleHelper.sendTitle(player, "§a§lSUA VEZ!", "§fDigite §b/dados §fpara jogar os dados", 0, 5000, 0);
			player.sendMessage("§a§lSUA VEZ! §fDigite §b/dados §fpara jogar os dados");
			player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, Sound.Source.PLAYER, 0.5f, 1));
		} else {
			new ArrestMenu(player).displayTo(player);
		}
    }

}
