package me.zombie_striker.gsl.recipes;

import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.materials.CustomMaterialType;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.megabuilds.MegaBuildType;
import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FactoryRecipe {

    private static List<FactoryRecipe> factoryRecipeList = new LinkedList<>();

    public static void init(){

        File customMaterialsDir = FileUtils.getFolder(FileUtils.PATH_RECIPES);
        if(!customMaterialsDir.exists())
            customMaterialsDir.mkdirs();

        YamlParserLoader customItems = new YamlParserLoader()
                .addDefault("name","AUTO_GENEREATED")
                .addDefault("displayname","displayname")
                .addDefault("factory","type")
                .addDefault("icon","DIRT")
                .addOptional("ingredients.DIRT",1)
                .addOptional("results.DIRT",1);

        for(File file : customMaterialsDir.listFiles()){
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if(!customItems.verifyAllPathsAreThere(fc)){
                customItems.addDefaultValues(fc,file);
            }
            String name = fc.getString("name");
            String displayname = fc.getString("displayname");
            String group = fc.getString("factory");
            MaterialType materialType = MaterialType.getMaterialType(fc.getString("icon"));

            FactoryRecipe factoryRecipe = new FactoryRecipe(name,displayname,group,materialType);

            if(fc.contains("ingredients")){
                for(String s : fc.getConfigurationSection("ingredients").getKeys(false)){
                    int i = fc.getInt("ingredients."+s);
                    factoryRecipe.getIngredients().put(MaterialType.getMaterialType(s),i);
                }
            }
            if(fc.contains("results")){
                for(String s : fc.getConfigurationSection("results").getKeys(false)){
                    int i = fc.getInt("results."+s);
                    factoryRecipe.getResults().put(MaterialType.getMaterialType(s),i);
                }
            }
            factoryRecipeList.add(factoryRecipe);

        }
    }

    public static List<FactoryRecipe> getFactoryRecipeList() {
        return factoryRecipeList;
    }

    private HashMap<MaterialType, Integer> ingredients = new HashMap<>();
    private HashMap<MaterialType, Integer> results = new HashMap<>();
    private String name;
    private String displayname;
    private String group;
    private MaterialType icon;

    public FactoryRecipe(String name, String displayname, String group, MaterialType icon){
        this.name = name;
        this.displayname = displayname;
        this.group = group;
        this.icon=icon;
    }

    public String getGroup() {
        return group;
    }

    public MaterialType getIcon() {
        return icon;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getName() {
        return name;
    }

    public HashMap<MaterialType, Integer> getIngredients() {
        return ingredients;
    }

    public HashMap<MaterialType, Integer> getResults() {
        return results;
    }
}
