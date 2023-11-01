package com.heyyczer.monopolyfarming.controller;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class BuildController {

    public static void pasteSchematic(Location location, List<String> schematics) {
        final int[] index = {0};

        Bukkit.getScheduler().runTaskTimer(SimplePlugin.getInstance(), task -> {
            final String schematic = schematics.get(index[0]);

            final File file = new File(SimplePlugin.getInstance().getDataFolder().getAbsolutePath(), "schematics/" + schematic + ".schem");

            ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);
            Clipboard clipboard;

            if (location.getWorld() == null) throw new NullPointerException("Failed to paste schematic due to world being null");

            BlockVector3 blockVector3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            if (clipboardFormat != null) {
                try (ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(file))) {
                    World world = BukkitAdapter.adapt(location.getWorld());
                    clipboard = clipboardReader.read();

                    AffineTransform transform = new AffineTransform();

                    double rotation = 0.0;
                    int yaw = Math.round(location.getYaw() / 90) * 90;

                    if (yaw == 90)
                        rotation = 0.0;

                    else if (yaw == 180.0 || yaw == -180.0)
                        rotation = -90.0;

                    else if (yaw == -90)
                        rotation = -180.0;

                    else if (yaw == 0)
                        rotation = 90.0;

                    transform = transform.rotateY(rotation);

                    clipboard.paste(world, blockVector3,  false, true, true, transform);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            index[0]++;

            if (index[0] >= schematics.size()) {
                task.cancel();
            }
        }, 20L, 20L);
    }

}
