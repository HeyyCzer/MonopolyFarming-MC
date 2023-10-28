package com.heyyczer.monopolyfarming.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Tile {

	private int index;

	private Location location;

	private String name;

	@Setter
	private List<String> description;

	@Setter
	private int price;

	@Setter
	private int rent;

	@Setter
	private int sell;

	private TileType type = TileType.PURCHASABLE;

	@Setter
	private Property property;

	@Setter
	private Special special;

	@Setter
	private Corner corner;

	public Tile(int index, Location location, String name, TileType type) {
		this.index = index;
		this.location = location;
		this.name = name;
		this.type = type;
	}

	public void buildHologram(UUID uuid) {
		final Location holoLoc = this.getLocation().clone().add(0.5f, 3f, 0.5f);

		Player owner = null;
		final List<String> hologramLines = new ArrayList<>();
		final List<String> ownedHologramLines = new ArrayList<>();

		switch (this.getType()) {
			case CORNER:
				hologramLines.add("&e&l" + this.getName());
				hologramLines.addAll(this.getDescription());
				break;
			case PURCHASABLE:
				if (this.getProperty().getOwner() == null) {
					hologramLines.add("&a&lPropriedade (À venda)");
					hologramLines.add("&7" + this.getName());
					hologramLines.add("&fProprietário: &8Ninguém");
					hologramLines.add("&fPreço: &a$" + this.getPrice());
					hologramLines.add("&fAluguel: &a$" + this.getRent());
					hologramLines.add(
							"#ICON: PLAYER_HEAD (eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=)");
				} else {
					owner = this.getProperty().getOwner().getPlayer();

					hologramLines.add("&d&lPropriedade");
					hologramLines.add("&7" + this.getName());
					hologramLines.add("&fProprietário: &d" + owner.getName());
					hologramLines.add("&fPreço: &d$" + this.getPrice());
					hologramLines.add("&fAluguel: &d$" + this.getRent());
					hologramLines.add("#ICON: PLAYER_HEAD (" + owner.getName() + ")");

					ownedHologramLines.add("&b&lSua propriedade");
					ownedHologramLines.add("&7" + this.getName());
					ownedHologramLines.add("&fVenda: &b$" + this.getSell());
					ownedHologramLines.add("&fAluguel: &b$" + this.getRent());
					ownedHologramLines.add("#ICON: PLAYER_HEAD (" + owner.getName() + ")");
				}
				break;
			case SPECIAL:
				if (this.getSpecial().getOwner() == null) {
					hologramLines.add("&e" + this.getName());
					hologramLines.addAll(this.getDescription());
				} else {
					owner = this.getProperty().getOwner().getPlayer();

					hologramLines.add("&e" + this.getName());
					hologramLines.addAll(this.getDescription());
				}
				break;
		}
		
		final String hologramId = uuid + "-tile-" + this.getIndex();
		final Hologram holo = DHAPI.getHologram(hologramId);

		if (holo != null) {
			DHAPI.setHologramLines(holo, hologramLines);
		} else {
			DHAPI.createHologram(hologramId, holoLoc, hologramLines);
		}

		Hologram ownedHolo = DHAPI.getHologram(hologramId + "-owned");
		if (ownedHolo != null) {
			DHAPI.setHologramLines(ownedHolo, ownedHologramLines);
		} else if (!ownedHologramLines.isEmpty()) {
			ownedHolo = DHAPI.createHologram(hologramId + "-owned", holoLoc, ownedHologramLines);
			ownedHolo.setDefaultVisibleState(false);
			ownedHolo.show(owner, 1);
		}
	}

}
