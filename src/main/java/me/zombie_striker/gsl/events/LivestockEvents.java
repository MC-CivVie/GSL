package me.zombie_striker.gsl.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class LivestockEvents implements Listener {

    @EventHandler
    public void onChickenLayEgg(EntityDropItemEvent event){
        if(event.getEntity().getType()== EntityType.CHICKEN&&event.getItemDrop().getItemStack().getType()== Material.EGG){
            ItemStack is = event.getItemDrop().getItemStack();
            is.setType(Material.FEATHER);
            event.getItemDrop().setItemStack(is);
        }
    }
}
