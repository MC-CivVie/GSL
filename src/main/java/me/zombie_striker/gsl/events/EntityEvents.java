package me.zombie_striker.gsl.events;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import me.zombie_striker.gsl.entities.EntityData;
import me.zombie_striker.gsl.materials.MaterialType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class EntityEvents implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        EntityData entityData = EntityData.getEntityData(event.getEntityType());
        if(entityData!=null){

            event.getDrops().clear();

            for(MaterialType materialType: entityData.getDropsPersistant()){
                event.getDrops().add(materialType.toItemStack());
            }
            for(Map.Entry<MaterialType, Double> e: entityData.getPercentageDrop().entrySet()){
                if(Math.random() < e.getValue()) {
                    event.getDrops().add(e.getKey().toItemStack());
                }
            }
        }
    }
    @EventHandler
    public void onPreSpawn(PreCreatureSpawnEvent event){
        if(event.getReason()!= CreatureSpawnEvent.SpawnReason.CUSTOM){
            EntityData entityData = EntityData.getEntityData(event.getType());
            if(entityData==null){
                event.setShouldAbortSpawn(true);
                return;
            }
            if(!entityData.canNaturallySpawn()){
                if(event.getReason()== CreatureSpawnEvent.SpawnReason.NATURAL){
                    event.setShouldAbortSpawn(true);
                }
            }
        }
    }
}
