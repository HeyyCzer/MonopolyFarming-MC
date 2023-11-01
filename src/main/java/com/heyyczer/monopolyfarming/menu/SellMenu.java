package com.heyyczer.monopolyfarming.menu;

import com.heyyczer.monopolyfarming.controller.BuildController;
import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.helper.NumberHelper;
import com.heyyczer.monopolyfarming.model.*;
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

import java.util.*;

public class SellMenu extends Menu {

    private final Map<Integer, Button> buttons = new HashMap<>();

    private boolean clicked = false;

    private Runnable onSold;

    public SellMenu(Player player, Runnable onSold) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        final Optional<GamePlayer> gamePlayer = gameRoom.getPlayers()
                .stream().filter(p -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();

        setTitle("&8Vender");
        setSize(9 * 6);

        this.onSold = onSold;

        final Tile currentTile = gameRoom.getTiles().get(gamePlayer.get().getPosition());

        int slot = 0;
        for (Tile tile : gameRoom.getPlayerTiles(player)) {
            buttons.put(slot, Button.makeSimple(
                    ItemCreator.of(CompMaterial.BOOK)
                            .name("&a" + tile.getName())
                            .lore(
                                    "&fValor de Venda: &b$" + NumberHelper.format(tile.getSell()),
                                    "",
                                    "&fSeu saldo: &e$" + NumberHelper.format(gamePlayer.get().getBalance()),
                                    "&fAinda faltam: &e$" + NumberHelper.format((long) currentTile.getRent() - gamePlayer.get().getBalance()),
                                    "",
                                    "&eClique para vender"
                            ),

                    p -> {
                        if (tile.getType() == TileType.PURCHASABLE) {
                            tile.getProperty().setOwner(null);
                            tile.getProperty().setCrops(new ArrayList<>());

                        } else if (tile.getType() == TileType.SPECIAL) {
                            tile.getSpecial().setOwner(null);
                        }

                        if (!tile.getSchematics().isEmpty()) {
                            BuildController.pasteSchematic(tile.getLocation(), Collections.singletonList("slot"));
                        }

                        gamePlayer.get().addBalance(tile.getSell());

                        gameRoom.getPlayers().forEach(pl -> {
                            if (pl.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
                            pl.getPlayer().sendMessage("§6" + player.getName() + " §evendeu a propriedade §6" + tile.getName() + " §epor §6$" + NumberHelper.format(tile.getSell()));
                        });

                        player.playSound(Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.PLAYER, 1f, 1f));
                        player.sendMessage("§aVocê vendeu a propriedade §b" + tile.getName() + " §apor §b$" + NumberHelper.format(tile.getSell()));

                        if (gamePlayer.get().getBalance() >= currentTile.getRent()) {
                            clicked = true;

                            gamePlayer.get().subtractBalance(currentTile.getRent());

                            this.onSold.run();

                            gameRoom.getPlayers().forEach(pl -> {
                                if (pl.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
                                pl.getPlayer().sendMessage("§6" + player.getName() + " §econseguiu pagar o aluguel. Foi por pouco!");
                            });
                            player.sendMessage("§aVocê conseguiu pagar o aluguel. Foi por pouco!");
                        }

                        getInventory().close();

                        tile.buildHologram(gameRoom.getUuid());
                    }));
            slot++;
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
        if (gameRoom != null && !clicked) {
            new SellMenu(player, this.onSold).displayTo(player);
        }
    }

}
