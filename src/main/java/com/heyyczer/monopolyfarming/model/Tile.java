package com.heyyczer.monopolyfarming.model;

import com.heyyczer.monopolyfarming.helper.NumberHelper;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Tile {

	private final int index;

	private final Location location;

	private final String name;

	@Setter
	private List<String> description;

	@Setter
	private List<String> schematics;

	@Setter
	private int price;

	@Setter
	private int rent;

	@Setter
	private int sell;

	private final TileType type;

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
		final Location holoLoc = this.getLocation().clone().add(0.5f, 3.25f, 0.5f);

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
					hologramLines.add("&fPreço: &a$" + NumberHelper.format(this.getPrice()));
					hologramLines.add("&fAluguel: &a$" + NumberHelper.format(this.getRent()));
					hologramLines.add(
							"#ICON: PLAYER_HEAD (eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=)");
				} else {
					owner = this.getProperty().getOwner().getPlayer();

					StringBuilder crops = new StringBuilder();
//					3x Trigo | 1x Batata

					for (Crop crop : this.getProperty().getCrops().stream().distinct().toList()) {
						if (!crops.isEmpty()) {
							crops.append(" &6| ");
						}
						crops.append("&e").append(this.getProperty().getCrops().stream().filter(c -> c == crop).count()).append("x ").append(crop.getName());
					}

					hologramLines.add("&d&lPropriedade");
					hologramLines.add("&7" + this.getName());
					hologramLines.add(crops.toString());
					hologramLines.add("&fProprietário: &d" + owner.getName());
					hologramLines.add("&fAluguel: &d$" + NumberHelper.format(this.getRent()));
					hologramLines.add("#ICON: PLAYER_HEAD (" + owner.getName() + ")");

					ownedHologramLines.add("&b&lSua propriedade");
					ownedHologramLines.add("&7" + this.getName());
					ownedHologramLines.add(crops.toString());
					ownedHologramLines.add("&fVenda: &b$" + NumberHelper.format(this.getSell()));
					ownedHologramLines.add("&fAluguel: &b$" + NumberHelper.format(this.getRent()));
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
		Hologram holo = DHAPI.getHologram(hologramId);

		if (holo != null) {
			DHAPI.setHologramLines(holo, hologramLines);
		} else {
			holo = DHAPI.createHologram(hologramId, holoLoc, hologramLines);
		}

		Hologram ownedHolo = DHAPI.getHologram(hologramId + "-owned");
		if (ownedHolo != null) {
			DHAPI.setHologramLines(ownedHolo, ownedHologramLines);
		} else if (!ownedHologramLines.isEmpty()) {
			ownedHolo = DHAPI.createHologram(hologramId + "-owned", holoLoc, ownedHologramLines);
			ownedHolo.setDefaultVisibleState(false);
			ownedHolo.setShowPlayer(owner);
			holo.setHidePlayer(owner);
		}
	}
	
	public void onPlayerLand(GamePlayer player, int diceValue) {
		switch (this.getType()) {
			case CORNER:
				this.getCorner().getHandler().onPlayerLand(player, this, diceValue);
				break;
			case PURCHASABLE:
				this.getProperty().onPlayerLand(player, this, diceValue);
				break;
			case SPECIAL:
				this.getSpecial().getHandler().onPlayerLand(player, this, diceValue);
				break;
		}
	}

	public GamePlayer getOwner() {
        return switch (this.getType()) {
            case PURCHASABLE -> this.getProperty().getOwner();
            case SPECIAL -> this.getSpecial().getOwner();
            default -> null;
        };
	}

}
