package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.dependancies.DependancyManager;
import me.zombie_striker.gsl.dependancies.NoteBlockAPIManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class NoteBlockSongEvents implements @NotNull Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(!event.getTo().getBlock().equals(event.getFrom().getBlock())){
            if(DependancyManager.hasNoteblockAPI())
                NoteBlockAPIManager.updateNearby(event.getPlayer());
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getClickedBlock()==null)
            return;
        if(event.getPlayer().getInventory().getItemInMainHand()==null)
            return;
        if(event.getClickedBlock().getType()!= Material.JUKEBOX)
            return;
        if(!DependancyManager.hasNoteblockAPI())
            return;
        if(NoteBlockAPIManager.alreadyPlayingSong(event.getClickedBlock().getLocation())) {
            NoteBlockAPIManager.getSongAt(event.getClickedBlock().getLocation()).destroy();
            return;
        }

        NoteBlockAPIManager.playNoteBlockSong("mother",event.getClickedBlock().getLocation());
    }
}
