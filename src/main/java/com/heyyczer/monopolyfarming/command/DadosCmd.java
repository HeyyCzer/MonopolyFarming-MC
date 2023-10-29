package com.heyyczer.monopolyfarming.command;

import org.bukkit.entity.Player;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.helper.DiceHelper;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import com.heyyczer.monopolyfarming.model.Tile;
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

                    player.sendMessage("§aVocê tirou §b" + number1 + " §ae §b" + number2 + " §anos dados. Andando §b" + total + " §acasas...");

                    room.getPlayers().forEach(p -> {
                        if (p.getPlayer().getUniqueId() == player.getUniqueId()) return;

                        p.getPlayer().sendMessage("§6" + player.getName() + " §etirou §6" + number1 + " §ee §6" + number2 + " §enos dados. Andando §6" + total + " §ecasas...");
					});
					
					currentPlayer.move(total, () -> {
						Tile tile = room.getTiles().get(currentPlayer.getPosition());
						tile.onPlayerLand(currentPlayer, total);
					});
                })
                .register();
    }

}
