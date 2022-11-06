package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FossilHandler {

    private static HashMap<ItemStack, Double> chance  = new HashMap<>();

    public static void init(){
        chance.put(new ItemStack(Material.DIRT),50.0);
        chance.put(new ItemStack(Material.IRON_INGOT),5.0);
        chance.put(new ItemStack(Material.DIAMOND),1.0);

        File music = FileUtils.getFolder(FileUtils.PATH_SONGS);
        for(File f : music.listFiles()){
            String name = f.getName().split("\\.")[0];
            ItemStack is = ItemUtil.prepareItem(Material.MUSIC_DISC_11,"Music Disc", new ComponentBuilder(name,ComponentBuilder.LIGHT_BLUE).build());
            chance.put(is,1.0);
        }


        chance.put(new ItemStack(Material.OBSIDIAN),1.0);
    }
    public static ItemStack getRandomItem(){
        double max = 0;
        for(Double d : chance.values())
            max+=d;
        max*=Math.random();
        for(Map.Entry<ItemStack, Double> e : chance.entrySet()){
            max-=e.getValue();
            if(max<=0){
                return e.getKey();
            }
        }
        return (ItemStack) chance.keySet().toArray()[0];
    }
}
