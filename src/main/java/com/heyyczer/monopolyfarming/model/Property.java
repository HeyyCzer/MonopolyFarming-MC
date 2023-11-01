package com.heyyczer.monopolyfarming.model;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.helper.NumberHelper;
import com.heyyczer.monopolyfarming.menu.PurchaseMenu;
import com.heyyczer.monopolyfarming.menu.SellMenu;
import com.heyyczer.monopolyfarming.menu.UpgradeMenu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Property {

    @Nullable
    @Setter
	private GamePlayer owner;

	@Setter
	private List<Crop> crops = new ArrayList<>();
	
	public void onPlayerLand(GamePlayer player, Tile tile, int diceValue) {
		// Can purchase
		if (owner == null) {
			new PurchaseMenu(player.getPlayer()).displayTo(player.getPlayer());


		// Is the owner
		} else if (owner.equals(player)) {
			new UpgradeMenu(player.getPlayer()).displayTo(player.getPlayer());

		// Is owned by someone else
		} else {
			GameRoom gameRoom = GameController.getGames().get(player.getGameUUID());
			if (player.getBalance() < tile.getRent()) {
				gameRoom.getPlayers().forEach(p -> {
					if (p.getPlayer().getUniqueId() == player.getPlayer().getUniqueId()) return;

					p.getPlayer().sendMessage("§c" + player.getPlayer().getName() + " §fnão tem dinheiro suficiente para pagar o aluguel da propriedade §c" + tile.getName() + "§f.");
				});

				new SellMenu(player.getPlayer(), () -> {
					gameRoom.getTurnController().nextPlayer();
					gameRoom.getTurnController().setWaiting(false);
				}).displayTo(player.getPlayer());
				return;
			}

			player.subtractBalance(tile.getRent());
			owner.addBalance(tile.getRent());

			player.getPlayer().playSound(Sound.sound(org.bukkit.Sound.ENTITY_CREEPER_HURT, Sound.Source.PLAYER, 0.5f, 1));
			owner.getPlayer().playSound(Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, Sound.Source.PLAYER, 0.5f, 1));

			player.getPlayer().sendMessage("§c§lPAGO §fVocê caiu na propriedade §e" + tile.getName() + " §fde §e" + owner.getPlayer().getName() + " §fe pagou §e$" + NumberHelper.format(tile.getRent()) + "§f.");
			owner.getPlayer().sendMessage("§a§lRECEBIDO §fO jogador §b" + player.getPlayer().getName() + " §fcaiu em sua propriedade §b" + tile.getName() + " §fe te pagou §b$" + NumberHelper.format(tile.getRent()) + "§f.");

			GameController.getGames().get(player.getGameUUID()).getTurnController().nextPlayer();
			GameController.getGames().get(player.getGameUUID()).getTurnController().setWaiting(false);
		}
	}

}
