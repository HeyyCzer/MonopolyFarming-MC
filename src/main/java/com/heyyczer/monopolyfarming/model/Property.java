package com.heyyczer.monopolyfarming.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.heyyczer.monopolyfarming.menu.PurchaseMenu;
import com.heyyczer.monopolyfarming.menu.UpgradeMenu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
			PurchaseMenu.show(player, tile);


		// Is the owner
		} else if (owner.equals(player)) {
			UpgradeMenu.show(player, tile);

		// Is owned by someone else
		} else {
			player.subtractBalance(tile.getRent());
			owner.addBalance(tile.getRent());

			player.getPlayer().sendMessage("§c§lPAGO §fVocê caiu na propriedade §e" + tile.getName() + " §fde §e" + owner.getPlayer().getName() + " §fe pagou §e$" + tile.getRent() + "§f.");
			owner.getPlayer().sendMessage("§a§lRECEBIDO §fO jogador §b" + player.getPlayer().getName() + " §fcaiu em sua propriedade §b" + tile.getName() + " §fe te pagou §b$" + tile.getRent() + "§f.");
		}
	}

}
