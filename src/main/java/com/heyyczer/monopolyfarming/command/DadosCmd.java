package com.heyyczer.monopolyfarming.command;

import com.heyyczer.monopolyfarming.controller.DiceController;
import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import com.heyyczer.monopolyfarming.model.interfaces.ICommand;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

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

                    DiceController.rollDices(room, player);
                })
                .register();
    }

}
