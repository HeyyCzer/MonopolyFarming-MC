package com.heyyczer.monopolyfarming.menu;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.Crop;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.Tile;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UpgradeMenu extends Menu {

    private final Map<Integer, Button> buttons = new HashMap<>();

    public UpgradeMenu(Player player) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        final Optional<GamePlayer> gamePlayer = gameRoom.getPlayers()
                .stream().filter(p -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();

        final Tile tile = gameRoom.getTiles().get(gamePlayer.get().getPosition());

        setTitle("&8Melhorias");
        setSize(9 * 4);

        for (Crop crop : Crop.values()) {
            final boolean hasMoney = (gamePlayer.get().getBalance() >= crop.getUnitPrice());

            buttons.put(crop.getChestSlot(), Button.makeSimple(
                    ItemCreator.of(hasMoney ? crop.getIcon() : CompMaterial.RED_CONCRETE.toItem())
                            .name((hasMoney ? "&a" : "&c") + crop.getName())
                            .glow(tile.getProperty().getCrops().contains(crop))
                            .lore(
                                    "&fQuant. Adquirida: &b"
                                            + tile.getProperty().getCrops().stream().filter(c -> c == crop).count()
                                            + "x",
                                    "&fPreço: &b$" + crop.getUnitPrice(),
                                    "",
                                    "&eClique para aprimorar"),

                    p -> {
                        if (gamePlayer.get().getBalance() < crop.getUnitPrice()) {
                            p.sendMessage("§cVocê não tem dinheiro suficiente para comprar essa melhoria!");
                            return;
                        }

                        getInventory().close();

                        gamePlayer.get().subtractBalance(crop.getUnitPrice());
                        tile.getProperty().getCrops().add(crop);

                        p.sendMessage("§aVocê colocou §b" + crop.getName() + " §aem sua propriedade!");
                        p.playSound(Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, Sound.Source.PLAYER, 0.5f, 1));

                        tile.buildHologram(gameRoom.getUuid());
                    }));
        }
    }

    @Override
    protected void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {
        if (buttons.containsKey(slot)) {
            Button button = buttons.get(slot);
            button.onClickedInMenu(player, this, click);
        }
    }

    @Override
    public ItemStack getItemAt(int slot) {
        if (buttons.containsKey(slot)) {
            return buttons.get(slot).getItem();
        }
        return NO_ITEM;
    }

    @Override
    protected void onMenuClose(Player player, Inventory inventory) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        gameRoom.getTurnController().nextPlayer();
        gameRoom.getTurnController().setWaiting(false);
    }

}
