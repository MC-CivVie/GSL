package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.handlers.CombatTagHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class CombatLogEvents implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(CombatTagHandler.isTagged(event.getPlayer().getUniqueId())){
            for(ItemStack is : event.getPlayer().getInventory().getContents()){
                if(is!=null)
                    event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(),is);
            }
            event.getPlayer().getInventory().clear();
            event.getPlayer().setHealth(0);
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            CombatTagHandler.addTag(event.getEntity().getUniqueId());
            CombatTagHandler.addTag(event.getDamager().getUniqueId());
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        //TODO: Set player health to 0 and clear inv on join again
    }
}
