package com.heyyczer.monopolyfarming.helper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;

public class TitleHelper {

	public static void sendTitle(Player player, String title, String subtitle, long fadeIn, long stay, long fadeOut) {
		player.showTitle(Title.title(
				Component.text(title),
				Component.text(subtitle),
				Title.Times.times(
						Duration.ofMillis(fadeIn),
						Duration.ofMillis(stay),
						Duration.ofMillis(fadeOut)
				)
		));
	}
	
}
