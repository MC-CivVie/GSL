package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class DefaultRecipeHandler {

    public static void removeBlacklistedCraftingRecipes(){
        YamlParserLoader parserLoader = new YamlParserLoader().addDefault("blacklist_materials", Arrays.asList(Material.BEDROCK.name()));

        File f = FileUtils.getFolder(FileUtils.PATH_CRAFTING_FILE);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (File file : f.listFiles()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if (parserLoader.verifyAllPathsAreThere(fc))
                parserLoader.addDefaultValues(fc, file);
            for(String s : fc.getStringList("blacklist_materials")){
                Material material = Material.matchMaterial(s);
                if(material!=null){
                    Iterator<Recipe> ri = Bukkit.recipeIterator();
                    for(Recipe recipe; ri.hasNext(); ){
                        recipe = ri.next();
                        if(recipe.getResult().getType()==material)
                            ri.remove();
                    }
                }
            }
        }
    }
}
