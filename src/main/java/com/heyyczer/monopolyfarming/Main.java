package com.heyyczer.monopolyfarming;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.heyyczer.monopolyfarming.command.DadosCmd;
import com.heyyczer.monopolyfarming.event.ExitListener;
import com.heyyczer.monopolyfarming.event.JoinListener;
import com.heyyczer.monopolyfarming.job.GameStarter;
import com.heyyczer.monopolyfarming.model.interfaces.ICommand;

import lombok.Getter;
import lombok.Setter;

public final class Main extends JavaPlugin {

    @Getter @Setter
	public static Plugin plugin = null;
	
    @Getter @Setter
    private static YamlConfiguration tilesConfig;

    private static final List<ICommand> COMMANDS = List.of(
//            new CreateProperty()
            new DadosCmd()
    );

    @Override
    public void onEnable() {
		Main.setPlugin(this);
        GameStarter.startRunnable();

        // Connect to database
        // ConnectionFactory.open();

        for (ICommand command : COMMANDS)
            command.register();

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ExitListener(), this);

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
