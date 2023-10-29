package com.heyyczer.monopolyfarming.menu;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.Crop;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.Tile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UpgradeMenu extends Menu {

    private final Map<Integer, Button> BUTTONS = new HashMap<>();

    public UpgradeMenu(Player player) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        final Optional<GamePlayer> gamePlayer = gameRoom.getPlayers()
                .stream().filter(p -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();

        final Tile tile = gameRoom.getTiles().get(gamePlayer.get().getPosition());

        setTitle("&8Melhorias");
        setSize(9 * 5);

        for (Crop crop : Crop.values()) {
            BUTTONS.put(crop.getChestSlot(), Button.makeSimple(
                    ItemCreator.of(crop.getIcon())
                            .name("&a" + crop.getName())
                            .lore(
                                    "&fQuant. Adquirida: &b"
                                            + tile.getProperty().getCrops().stream().filter(c -> c == crop).count()
                                            + "x",
                                    "&fPreço: &b$" + crop.getUnitPrice(),
                                    "",
                                    "&eClique para aprimorar"),

                    p -> {
                        if (gamePlayer.get().getBalance() < crop.getUnitPrice()) {
                            p.sendMessage("§cVocê não tem dinheiro suficiente para comprar esta melhoria!");
                            return;
                        }

                        getInventory().close();

                        gamePlayer.get().subtractBalance(crop.getUnitPrice());
                        tile.getProperty().getCrops().add(crop);

                        p.sendMessage("§aVocê colocou §b" + crop.getName() + " §aem sua propriedade!");

                        tile.buildHologram(gameRoom.getUuid());
                    }));
        }
    }

    @Override
    public ItemStack getItemAt(int slot) {
        return BUTTONS.get(slot).getItem();
    }

    @Override
    protected void onMenuClose(Player player, Inventory inventory) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        gameRoom.getTurnController().nextPlayer();
        gameRoom.getTurnController().setWaiting(false);
    }

}
