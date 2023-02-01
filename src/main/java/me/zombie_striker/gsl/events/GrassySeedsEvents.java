package me.zombie_striker.gsl.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class GrassySeedsEvents implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getBlock().getType()== Material.GRASS){
            if( ThreadLocalRandom.current().nextInt(100)==0){
                event.getBlock().getDrops().add(new ItemStack(Material.BEETROOT_SEEDS));
            }else if( ThreadLocalRandom.current().nextInt(100)==0){
                event.getBlock().getDrops().add(new ItemStack(Material.MELON_SEEDS));
            }else if( ThreadLocalRandom.current().nextInt(100)==0){
                event.getBlock().getDrops().add(new ItemStack(Material.PUMPKIN_SEEDS));
            }else if( ThreadLocalRandom.current().nextInt(100)==0){
                event.getBlock().getDrops().add(new ItemStack(Material.POTATO));
            }else if( ThreadLocalRandom.current().nextInt(100)==0){
                event.getBlock().getDrops().add(new ItemStack(Material.CARROT));
            }
        }
    }
}
