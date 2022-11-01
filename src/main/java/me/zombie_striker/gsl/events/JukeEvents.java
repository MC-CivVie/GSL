package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.snitches.*;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import me.zombie_striker.gsl.world.GSLWorld;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class JukeEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreakSnitch(BlockBreakEvent event) {
        GSLWorld gslWorld = GSLWorld.getWorld(event.getBlock().getWorld());
        for (Snitch snitch : new ArrayList<>(gslWorld.getSnitches())) {
            if (snitch.getLocation().equals(event.getBlock().getLocation())) {
                gslWorld.unregisterSnitch(snitch);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceSnitch(BlockPlaceEvent event) {
        GSLChunk gslChunk = GSLChunk.getGSLChunk(event.getBlock().getChunk());
        GSLCube gslCube = gslChunk.getCubes()[(event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
        if (gslCube != null) {
            int x = event.getBlock().getX() % 16;
            if (event.getBlock().getX() < 0)
                x = Math.abs((-event.getBlock().getX()) % 16 - 15);
            int z = event.getBlock().getZ() % 16;
            if (event.getBlock().getZ() < 0)
                z = Math.abs((-event.getBlock().getZ()) % 16 - 15);

            int y = event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET;

            if (gslCube.getNamelayers()[x][y][z] != null) {
                Snitch snitch = new Snitch(event.getBlock().getLocation(), Snitch.SNITCHLOG_SIZE);
                GSLWorld.getWorld(event.getBlock().getWorld()).registerSnitch(snitch);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceAnything(BlockPlaceEvent event) {
        GSLWorld gslWorld = GSLWorld.getWorld(event.getBlockAgainst().getWorld());
        SnitchLogPlace placelog = new SnitchLogPlace(event.getBlock().getType(), System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getBlock().getLocation());
        for (Snitch snitch : gslWorld.getSnitches()) {
            if (snitch.canDetect(event.getBlock().getLocation())) {
                snitch.addToLog(placelog);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreakAnything(BlockPlaceEvent event) {
        GSLWorld gslWorld = GSLWorld.getWorld(event.getBlockAgainst().getWorld());
        SnitchLogBreak placelog = new SnitchLogBreak(event.getBlock().getType(), System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getBlock().getLocation());
        for (Snitch snitch : gslWorld.getSnitches()) {
            if (snitch.canDetect(event.getBlock().getLocation())) {
                snitch.addToLog(placelog);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        GSLWorld gslWorld = GSLWorld.getWorld(event.getPlayer().getWorld());
        for (Snitch snitch : gslWorld.getSnitches()) {
            if (snitch.canDetect(event.getFrom()) && !snitch.canDetect(event.getTo())) {
                SnitchLogUnauthorizedExit placelog = new SnitchLogUnauthorizedExit(Material.PLAYER_HEAD, System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
                snitch.addToLog(placelog);
            } else if (!snitch.canDetect(event.getFrom()) && snitch.canDetect(event.getTo())) {
                SnitchLogUnauthorizedEnter placelog = new SnitchLogUnauthorizedEnter(Material.PLAYER_HEAD, System.currentTimeMillis(), event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
                snitch.addToLog(placelog);
            }
        }
    }
}
