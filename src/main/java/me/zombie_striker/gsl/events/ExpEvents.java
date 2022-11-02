package me.zombie_striker.gsl.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class ExpEvents implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent event){
        event.setExpToDrop(0);
    }
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event){
        event.setDroppedExp(0);
    }
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event){
        event.setExpToDrop(0);
    }
}
