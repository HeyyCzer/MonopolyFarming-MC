package com.heyyczer.monopolyfarming.command;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.helper.DiceHelper;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import com.heyyczer.monopolyfarming.model.interfaces.ICommand;

import dev.jorel.commandapi.CommandAPICommand;

public class DadosCmd implements ICommand {

    @Override
    public void register() {
        // Create our command
        new CommandAPICommand("dados")
                .executes((sender, args) -> {
                    Player player = (Player) sender;

                    GameRoom room = GameController.getGameRoomByPlayer(player);
                    if (room == null || room.getStatus() != GameStatus.STARTED) {
                        player.sendMessage("§cVocê não está em um jogo ou o jogo ainda não foi iniciado!");
                        return;
                    }

                    GamePlayer currentPlayer = room.getPlayers().get(room.getTurnController().CURRENT_PLAYER_INDEX);
                    if (currentPlayer.getPlayer().getUniqueId() != player.getUniqueId()) {
                        player.sendMessage("§cAinda não é seu turno para jogar!");
                        return;
                    }

                    if (room.getTurnController().isWaiting()) {
                        player.sendMessage("§cVocê já está se movimentando!");
                        return;
                    }

                    // randomize 2 dices (1-6)
                    int number1 = DiceHelper.getRandomNumber();
                    int number2 = DiceHelper.getRandomNumber();
                    int total = number1 + number2;

                    player.sendMessage("§aVocê tirou §f" + number1 + " §ae §f" + number2 + " §anos dados. Andando §f" + total + " §acasas...");
                    currentPlayer.getPlayer().setLevel(total);
                    room.getTurnController().setWaiting(true);

                    room.getPlayers().forEach(p -> {
                        if (p.getPlayer().getUniqueId() == player.getUniqueId()) return;

                        p.getPlayer().sendMessage("§6" + player.getName() + " §etirou §f" + number1 + " §ee §f" + number2 + " §enos dados. Andando §f" + total + " §ecasas...");
                    });

                    new BukkitRunnable() {
                        int walked = 0;

                        @Override
                        public void run() {
                            if (walked < total) {
                                walked++;
                                currentPlayer.setPosition((currentPlayer.getPosition() + 1) % room.getTiles().size());
                                currentPlayer.getPlayer().setLevel(total - walked);
                            } else {
                                currentPlayer.getPlayer().setExp(0);
                                room.getTurnController().nextPlayer();
                                room.getTurnController().setWaiting(false);
                                cancel();
                            }
                        }
                    }.runTaskTimer(Main.getPlugin(), 20L, 20);
                })
                .register();
    }

}
