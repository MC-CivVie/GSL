package me.zombie_striker.gsl.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import me.zombie_striker.gsl.world.GSLChunk;

public class ChunkLoadEvents implements Listener {

    @EventHandler
    public void onLoad(ChunkLoadEvent event){
        GSLChunk gsl = GSLChunk.load(event.getChunk());
        GSLChunk.register(gsl);
    }
    @EventHandler
    public void onUnload(ChunkUnloadEvent event){
        GSLChunk gsl = GSLChunk.getGSLChunk(event.getChunk());
        GSLChunk.unregister(gsl);
        gsl.save();
    }
}
