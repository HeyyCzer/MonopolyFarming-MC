package com.heyyczer.monopolyfarming.menu;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.Tile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.StartPosition;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Optional;

public class PurchaseMenu extends Menu {

    @Position(start = StartPosition.CENTER, value = -2)
    private Button purchaseButton;

    @Position(start = StartPosition.CENTER, value = 2)
    private Button cancelButton;

    public PurchaseMenu(Player player) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        final Optional<GamePlayer> gamePlayer = gameRoom.getPlayers()
                .stream().filter(p -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();

        final Tile tile = gameRoom.getTiles().get(gamePlayer.get().getPosition());

        setTitle("&8Comprar propriedade");
        setSize(9 * 3);

        this.purchaseButton = Button.makeSimple(
                ItemCreator.of(CompMaterial.PLAYER_HEAD)
                        .skullUrl("https://textures.minecraft.net/texture/209299a117bee88d3262f6ab98211fba344ecae39b47ec848129706dedc81e4f")
                        .name("&aComprar: &e" + tile.getName())
                        .lore(
                                "&fPreço: &b$" + tile.getPrice(),
                                "&fAluguel: &b$" + tile.getRent(),
                                "&fVenda: &b$" + tile.getSell(),
                                "",
                                "&eClique para adquirir"
                        ),

                p -> {
                    if (gamePlayer.get().getBalance() < tile.getPrice()) {
                        p.sendMessage("§cVocê não tem dinheiro suficiente para comprar este aprimoramento!");
                        return;
                    }

                    getInventory().close();

                    gamePlayer.get().subtractBalance(tile.getPrice());
                    tile.getProperty().setOwner(gamePlayer.get());

                    p.sendMessage("§aVocê comprou a propriedade §b" + tile.getName() + "§a!");

                    tile.buildHologram(gameRoom.getUuid());
                });

        this.cancelButton = Button.makeSimple(
                ItemCreator.of(CompMaterial.RED_CONCRETE)
                        .name("&cFechar")
                        .lore(
                                "",
                                "&eNão comprar e continuar jogando"
                        ),

                p -> getInventory().close());
    }

    @Override
    protected void onMenuClose(Player player, Inventory inventory) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        gameRoom.getTurnController().nextPlayer();
        gameRoom.getTurnController().setWaiting(false);
    }

}
