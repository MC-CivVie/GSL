package me.zombie_striker.gsl.utils.guis;

import me.zombie_striker.gsl.GSL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;
import java.util.List;

public class GUIUtil implements Listener {

    private static List<GUI> guis = new LinkedList<>();


    public static void init(){
        Bukkit.getPluginManager().registerEvents(new GUIUtil(),GSL.getCore());
    }

    public void register(GUI gui){
        guis.add(gui);
    }
    public void unregister(GUI gui){
        guis.remove(gui);
    }
    public static GUI getGUI(Inventory inventory){
        for(GUI g : guis){
            if(g.getInventory().equals(inventory))
                return g;
        }
        return null;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event){
        GUI gui;
        if((gui=getGUI(event.getInventory()))!=null) {
            event.setCancelled(true);
            if(gui.getActions()[event.getSlot()]!=null){
                gui.getActions()[event.getSlot()].click(event.getSlot(), (Player) event.getWhoClicked(),gui);
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent event){
        if((getGUI(event.getInventory()))!=null) {
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(InventoryMoveItemEvent event){
        if((getGUI(event.getSource()))!=null||(getGUI(event.getDestination()))!=null) {
            event.setCancelled(true);
        }
    }
}
