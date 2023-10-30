package com.heyyczer.monopolyfarming.menu;

import com.heyyczer.monopolyfarming.controller.BuildController;
import com.heyyczer.monopolyfarming.controller.DiceController;
import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.Tile;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.StartPosition;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Optional;

public class ArrestMenu extends Menu {

    @Position(start = StartPosition.CENTER, value = -2)
    private Button tryDices;

    @Position(start = StartPosition.CENTER, value = 2)
    private Button payButton;

    public ArrestMenu(Player player) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        final Optional<GamePlayer> gamePlayer = gameRoom.getPlayers()
                .stream().filter(p -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();

        final Tile tile = gameRoom.getTiles().get(gamePlayer.get().getPosition());

        setTitle("&8Comprar propriedade");
        setSize(9 * 3);

        this.tryDices = Button.makeSimple(
                ItemCreator.of(CompMaterial.PLAYER_HEAD)
                        .skullUrl("https://textures.minecraft.net/texture/a3135ea31bc15be13462bf10e912a140e5a7d68ef4bd26e3d750559d502bf95")
                        .name("&eTentar dados iguais")
                        .lore(
                                "§fJogue os dados, se os §bresultados §fforem §biguais",
                                "§fvocê será §bliberado da prisão§f, mas se não,",
                                "§fvocê perderá a vez.",
                                "§f",
                                "§eVocê está preso por §6" + gamePlayer.get().getSkips() + " jogadas"
                        ),

                p -> {
                    if (gamePlayer.get().getBalance() < tile.getPrice()) {
                        p.sendMessage("§cVocê não tem dinheiro suficiente para comprar este aprimoramento!");
                        return;
                    }

                    getInventory().close();

                    DiceController.rollDices(gameRoom, p);
                });

        this.payButton = Button.makeSimple(
                ItemCreator.of(CompMaterial.PLAYER_HEAD)
                        .skullUrl("https://textures.minecraft.net/texture/209299a117bee88d3262f6ab98211fba344ecae39b47ec848129706dedc81e4f")
                        .name("&ePagar fiança")
                        .lore(
                                "§fPague §b$" + tile.getPrice() + " §fpara sair da prisão",
                                "§f",
                                "§eVocê está preso por §6" + gamePlayer.get().getSkips() + " jogadas"
                        ),

                p -> {
                    if (gamePlayer.get().getBalance() < tile.getPrice()) {
                        p.sendMessage("§cVocê não tem dinheiro suficiente para comprar este aprimoramento!");
                        return;
                    }

                    getInventory().close();

                    gamePlayer.get().subtractBalance(tile.getPrice());

                    p.sendMessage("§aVocê pagou sua fiança, poderá jogar na próxima rodada.");
                    p.playSound(Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, Sound.Source.PLAYER, 0.5f, 1));
                });
    }

    @Override
    protected void onMenuClose(Player player, Inventory inventory) {
        final GameRoom gameRoom = GameController.getGameRoomByPlayer(player);
        gameRoom.getTurnController().nextPlayer();
        gameRoom.getTurnController().setWaiting(false);
    }

}
