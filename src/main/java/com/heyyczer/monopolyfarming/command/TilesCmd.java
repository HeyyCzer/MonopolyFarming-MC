package com.heyyczer.monopolyfarming.command;

import com.heyyczer.monopolyfarming.controller.GameController;
import com.heyyczer.monopolyfarming.model.GamePlayer;
import com.heyyczer.monopolyfarming.model.GameRoom;
import com.heyyczer.monopolyfarming.model.GameStatus;
import com.heyyczer.monopolyfarming.model.Tile;
import com.heyyczer.monopolyfarming.model.interfaces.ICommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Player;

public class TilesCmd implements ICommand {

    @Override
    public void register() {
        // Create our command
        new CommandAPICommand("tiles")
                .withOptionalArguments(
                    new IntegerArgument("total")
                )
                .withPermission(CommandPermission.OP)
                .executes((sender, args) -> {
                    Player player = (Player) sender;

                    GameRoom room = GameController.getGameRoomByPlayer(player);
                    if (room == null || room.getStatus() != GameStatus.STARTED) {
                        player.sendMessage("§cVocê não está em um jogo ou o jogo ainda não foi iniciado!");
                        return;
                    }

                    if (args.argsMap().containsKey("total")) {
                        Integer total = (Integer) args.get("total");
                        final GamePlayer gamePlayer = room.getPlayers().get(room.getTurnController().CURRENT_PLAYER_INDEX);

                        gamePlayer.move(total, () -> {
                            Tile tile = room.getTiles().get(gamePlayer.getPosition());
                            tile.onPlayerLand(gamePlayer, total);
                        });
                    }
                })
                .register();
    }

}
