package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.GSL;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WorldBorderEvents implements Listener {

    private int radius = 5000;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        double distanceFromCenter = new Location(event.getTo().getWorld(), 0, 0, 0).distanceSquared(event.getTo());
        if (distanceFromCenter >= radius * radius) {
            double distanceFromCenter2 = new Location(event.getTo().getWorld(), 0, 0, 0).distanceSquared(event.getFrom());
            if (distanceFromCenter > distanceFromCenter2)
                event.setCancelled(true);
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(!event.getPlayer().hasPlayedBefore())
            new BukkitRunnable(){
            @Override
                public void run() {
                    for(int i = 0; i < 25; i++){
                        int x = random();
                        int z = random();
                        Location loc = event.getPlayer().getLocation();
                        if(!loc.getWorld().isChunkGenerated(x/16,z/16))
                            continue;
                        loc=loc.getWorld().getHighestBlockAt(x,z).getLocation();
                        if(loc.getBlock().getType()== Material.WATER)
                            continue;
                        if(loc.getBlock().getType()== Material.LAVA)
                            continue;
                        if(loc.getBlock().getType()== Material.CACTUS)
                            continue;
                        if(loc.getBlock().getType()== Material.MAGMA_BLOCK)
                            continue;
                        if(loc.getBlock().getType().name().endsWith("LEAVES"))
                            continue;
                        event.getPlayer().teleport(loc.add(0,1,0));
                    }

                }
            }.runTaskLater(GSL.getCore(),20);
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        if(event.getPlayer().getBedSpawnLocation()==null)
        for(int i = 0; i < 25; i++){
            int x = random();
            int z = random();

            Location loc = event.getRespawnLocation();
            loc=loc.getWorld().getHighestBlockAt(x,z).getLocation();
            if(loc.getBlock().getType()== Material.WATER)
                continue;
            if(loc.getBlock().getType()== Material.LAVA)
                continue;
            if(loc.getBlock().getType()== Material.CACTUS)
                continue;
            if(loc.getBlock().getType()== Material.MAGMA_BLOCK)
                continue;
            if(loc.getBlock().getType().name().endsWith("LEAVES"))
                continue;
            double distanceFromCenter = new Location(loc.getWorld(), 0, 100, 0).distanceSquared(loc);
            if (distanceFromCenter >= radius * radius) {
                continue;
            }
            event.setRespawnLocation(loc.add(0,1,0));
            }
    }

    public int random(){
        int d = ((new Random().nextInt(2*radius)-radius));
        return d;
    }
}
