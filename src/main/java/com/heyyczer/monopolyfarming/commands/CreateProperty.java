package com.heyyczer.monopolyfarming.commands;

import com.heyyczer.monopolyfarming.Main;
import com.heyyczer.monopolyfarming.model.ICommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CreateProperty implements ICommand {

    @Override
    public void register() {
        File tilesYml = new File(Main.getPlugin().getDataFolder(), "tiles.yml");

        // Create our command
        new CommandAPICommand("addp")
                .withArguments(
                        new StringArgument("name"),
                        new StringArgument("type")
                ) // The arguments
                .withOptionalArguments(
                        new IntegerArgument("price"),
                        new StringArgument("schematic")
                )
                .withPermission(CommandPermission.OP) // Required permissions
                .executes((sender, args) -> {
                    Player player = (Player) sender;

                    Location location = player.getLocation();

                    YamlConfiguration config = Main.getTilesConfig();
                    Set<String> list = config.getConfigurationSection("locations").getKeys(false);

                    final int index = (list.size() + 1);
                    final String prefix = "locations.tile-" + index;

                    final String name = (String) args.get("name");
                    config.set(prefix + ".name", name.replace("_", " "));
                    config.set(prefix + ".type", args.get("type"));

                    if (args.get("price") != null) {
                        config.set(prefix + ".price", args.get("price"));
                    }
                    if (args.get("schematic") != null) {
                        config.set(prefix + ".schematic", args.get("schematic"));
                    }
                    config.set(prefix + ".location.x", location.getBlockX());
                    config.set(prefix + ".location.y", location.getBlockY());
                    config.set(prefix + ".location.z", location.getBlockZ());
                    config.set(prefix + ".location.yaw", location.getYaw());

                    try {
                        config.save(tilesYml);

                        player.sendMessage("§aCoordenada §e#" + index + " §asalva com sucesso!");

                        Main.setTilesConfig(config);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .register();
    }

}
