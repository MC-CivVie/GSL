package me.zombie_striker.gsl.events;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingEvents implements Listener {

    @EventHandler
    public void onFishEnchantment(PlayerFishEvent event){
        if(event.getCaught() instanceof Item){
            if(((Item) event.getCaught()).getItemStack().getType()== Material.ENCHANTED_BOOK)
                event.setCancelled(true);
        }
    }
}
