package me.zombie_striker.gsl.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WorldBorderEvents implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        double distanceFromCenter = new Location(event.getTo().getWorld(), 0, 0, 0).distanceSquared(event.getTo());
        if (distanceFromCenter >= 10000 * 10000) {
            double distanceFromCenter2 = new Location(event.getTo().getWorld(), 0, 0, 0).distanceSquared(event.getFrom());
            if (distanceFromCenter > distanceFromCenter2)
                event.setCancelled(true);
        }
    }
}
