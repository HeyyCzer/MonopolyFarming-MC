package com.heyyczer.monopolyfarming;

import com.heyyczer.monopolyfarming.database.ConnectionFactory;
import com.heyyczer.monopolyfarming.events.JoinListener;
import com.heyyczer.monopolyfarming.jobs.GameStarter;
import com.heyyczer.monopolyfarming.model.ICommand;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public final class Main extends JavaPlugin {

    @Getter @Setter
    public static Plugin plugin = null;

    @Getter @Setter
    private static YamlConfiguration tilesConfig;

    private static final List<ICommand> COMMANDS = List.of(
//            new CreateProperty()
    );

    @Override
    public void onEnable() {
        Main.setPlugin(this);
        GameStarter.startRunnable();

        // Connect to database
        ConnectionFactory.open();

        for (ICommand command : COMMANDS)
            command.register();

        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        File tilesYml = new File(this.getDataFolder(), "tiles.yml");
        if (!tilesYml.exists()) {
            tilesYml.getParentFile().mkdirs();
            saveResource("tiles.yml", false);
        }

        tilesConfig = YamlConfiguration.loadConfiguration(tilesYml);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
