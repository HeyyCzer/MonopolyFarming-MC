package com.heyyczer.monopolyfarming.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public enum Crop {
	
	WHEAT(new ItemStack(Material.WHEAT), "Trigo", 10000, 10),
	POTATO(new ItemStack(Material.POTATO), "Batata", 10000, 13),
	CARROT(new ItemStack(Material.CARROT), "Cenoura", 10000, 16),
	BEETROOT(new ItemStack(Material.BEETROOT), "Beterraba", 10000, 20),
	COW(new ItemStack(Material.BEEF), "Gado", 10000, 33);

	private ItemStack icon;

	private String name;

	private int unitPrice;

	private int chestSlot;

}
