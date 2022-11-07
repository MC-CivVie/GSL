package me.zombie_striker.gsl.events;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import me.zombie_striker.gsl.entities.EntityData;
import me.zombie_striker.gsl.materials.MaterialType;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EntityEvents implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityData entityData = EntityData.getEntityData(event.getEntityType());
        if (entityData != null) {
            event.getDrops().clear();
            for (Map.Entry<MaterialType, Double> e : entityData.getPercentageDrop().entrySet()) {
                if(e.getKey()!=null)
                if (Math.random() <= e.getValue()) {
                    ItemStack is = e.getKey().toItemStack();
                    if (entityData.getMaxAmountDrop().containsKey(e.getKey())) {
                        is.setAmount((int) (entityData.getMaxAmountDrop().get(e.getKey()) * Math.random()) + 1);
                    }
                    event.getDrops().add(is);
                }
            }
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if(!event.getEntityType().isAlive())
            return;
        EntityData entityData = EntityData.getEntityData(event.getEntityType());
        if (entityData == null) {
            event.setCancelled(true);
            return;
        }
        if (!entityData.canNaturallySpawn()) {
            event.setCancelled(true);
        }
    }
}
