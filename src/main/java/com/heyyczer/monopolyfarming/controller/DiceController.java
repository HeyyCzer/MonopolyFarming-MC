package com.heyyczer.monopolyfarming.controller;

import com.heyyczer.monopolyfarming.helper.DiceHelper;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.Tile;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

public class DiceController {

    public static void rollDices(GameRoom room, Player player) {
        GamePlayer currentPlayer = room.getPlayers().get(room.getTurnController().CURRENT_PLAYER_INDEX);

        if (currentPlayer.getPlayer().getUniqueId() != player.getUniqueId()) {
            player.sendMessage("§cAinda não é seu turno para jogar!");
            return;
        }

        if (room.getTurnController().isWaiting()) {
            player.sendMessage("§cAinda não é sua vez de jogar!");
            return;
        }

        // randomize 2 dices (1-6)
        int number1 = DiceHelper.getRandomNumber(System.currentTimeMillis());
        int number2 = DiceHelper.getRandomNumber(System.currentTimeMillis() + 10);
        int total = number1 + number2;

        boolean movePlayer = false;

        if (!currentPlayer.isArrested()) {
            player.sendMessage("§aVocê tirou §b" + number1 + " §ae §b" + number2 + " §anos dados. Andando §b" + total + " §acasas...");
            player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, Sound.Source.PLAYER, 0.5f, 1));
            movePlayer = true;
        } else {
            if (number1 == number2) {
                player.sendMessage("§a§lBOA! §fVocê optou por dados iguais e conseguiu! Os números foram §a" + number1 + " §fe §a" + number2 + "§f. Você está livre da cadeia!");
                player.playSound(Sound.sound(org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, Sound.Source.PLAYER, 0.5f, 1));
                currentPlayer.setSkips(0);
                currentPlayer.setArrested(false);
                currentPlayer.updatePlayer();
            } else {
                player.sendMessage("§c§lQUE PENA! §fVocê optou por dados iguais, mas os números foram §b" + number1 + " §fe §b" + number2 + "§f. Pulando sua vez...");
                player.playSound(Sound.sound(org.bukkit.Sound.ENTITY_ENDER_DRAGON_HURT, Sound.Source.PLAYER, 0.5f, 1));
            }
        }

        room.getPlayers().forEach(p -> {
            if (p.getPlayer().getUniqueId() == player.getUniqueId()) return;

            if (!currentPlayer.isArrested()) {
                p.getPlayer().sendMessage("§6" + player.getName() + " §etirou §6" + number1 + " §ee §6" + number2 + " §enos dados. Andando §6" + total + " §ecasas...");
            } else {
                if (number1 == number2) {
                    p.getPlayer().sendMessage("§d" + player.getName() + " §foptou por dados iguais e conseguiu! Os números foram §d" + number1 + " §fe §d" + number2 + "§f. Está livre da cadeia!");
                    p.getPlayer().playSound(Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, Sound.Source.PLAYER, 0.5f, 1));
                } else {
                    p.getPlayer().sendMessage("§e" + player.getName() + " §foptou por dados iguais, mas os números foram §e" + number1 + " §fe §e" + number2 + "§f. Pulando a vez...");
                    p.getPlayer().playSound(Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, Sound.Source.PLAYER, 0.5f, 1));
                }
            }
        });

        if (movePlayer) {
            currentPlayer.move(total, () -> {
                Tile tile = room.getTiles().get(currentPlayer.getPosition());
                tile.onPlayerLand(currentPlayer, total);
            });
        }
    }

}
