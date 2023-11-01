package com.heyyczer.monopolyfarming;

import com.heyyczer.monopolyfarming.command.DadosCmd;
import com.heyyczer.monopolyfarming.command.MoneyCmd;
import com.heyyczer.monopolyfarming.command.StuckCmd;
import com.heyyczer.monopolyfarming.command.TilesCmd;
import com.heyyczer.monopolyfarming.event.ExitListener;
import com.heyyczer.monopolyfarming.event.JoinListener;
import com.heyyczer.monopolyfarming.job.GameStarter;
import com.heyyczer.monopolyfarming.model.interfaces.ICommand;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.util.List;

public class Main extends SimplePlugin {

    @Getter @Setter
    private static YamlConfiguration tilesConfig;

    private static final List<ICommand> COMMANDS = List.of(
            new DadosCmd(),
            new StuckCmd(),
            new TilesCmd(),
            new MoneyCmd()
    );

    @Override
    public void onPluginStart() {
        GameStarter.startRunnable();

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
    public void onPluginStop() {
        // Plugin shutdown logic
    }

}
