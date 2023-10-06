package com.heyyczer.monopolyfarming.model;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class GameRoom {

    @NonNull
    private java.util.UUID UUID;

    @NonNull
    private List<GamePlayer> players;

    @Setter
    private int currentPlayerIndex = 0;

    @Setter
    private GameStatus status = GameStatus.STARTING;

    @Setter
    private int timeToStart = 10;

    public void startGame() {
        for (GamePlayer player : this.getPlayers()) {
            player.getPlayer().sendTitle(
                    "§e§lINICIANDO...",
                    "O mapa está sendo preparado, aguarde...",
                    0, 30 * 20, 0
            );
        }

        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        MVWorldManager worldManager = core.getMVWorldManager();

        final String worldName = "game-" + this.getUUID();
        worldManager.cloneWorld("game", worldName);

        Tile tile = tiles.get(0);
        final Location loc = new Location(Bukkit.getWorld(worldName), tile.getX(), tile.getY(), tile.getZ());

        for (GamePlayer player : this.getPlayers()) {
            player.getPlayer().teleport(loc);
            player.getPlayer().sendTitle(
                    "§b§lINICIADO",
                    "Tenha um ótimo jogo :D",
                    0, 10 * 20, 0
            );
        }
    }

}
