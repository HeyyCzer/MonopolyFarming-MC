package com.heyyczer.monopolyfarming.events;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text("§7[§a+§7] §a" + event.getPlayer().getName() + " §7entrou no jogo!"));
    }

}
