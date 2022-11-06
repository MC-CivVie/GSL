package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.dependancies.DependancyManager;
import me.zombie_striker.gsl.dependancies.NoteBlockAPIManager;
import me.zombie_striker.gsl.utils.FileUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
        if(event.getHand()== EquipmentSlot.OFF_HAND)
            return;
        if(event.getClickedBlock()==null)
            return;
        if(event.getClickedBlock().getType()!= Material.JUKEBOX)
            return;
        if(event.getItem()==null || !event.getItem().getType().isRecord())
            return;
        if(!DependancyManager.hasNoteblockAPI())
            return;
        if(NoteBlockAPIManager.alreadyPlayingSong(event.getClickedBlock().getLocation())) {
            NoteBlockAPIManager.getSongAt(event.getClickedBlock().getLocation()).setPlaying(false);
            return;
        }
        String name= null;
        for(Component name1 : event.getItem().lore()){
            if(new File(FileUtils.getFolder(FileUtils.PATH_SONGS),((TextComponent)name1).content()+".nbs").exists()){
                name=((TextComponent) name1).content();
                break;
            }
        }
        if(name!=null) {
            NoteBlockAPIManager.playNoteBlockSong(name, event.getClickedBlock().getLocation());
            event.setCancelled(true);
            Jukebox jukebox = (Jukebox) event.getClickedBlock().getState();
            jukebox.setRecord(event.getItem());
            jukebox.update();
            event.getPlayer().getInventory().setItemInMainHand(null);
        }
    }
}
