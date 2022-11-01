package me.zombie_striker.gsl.materials;

import me.zombie_striker.gsl.files.FileUtils;
import me.zombie_striker.gsl.files.YamlParserLoader;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MaterialType {


    private static HashMap<String, MaterialType> materialTypes = new HashMap<>();
    private static HashMap<String, GroupMaterialType> groupMaterialTypes = new HashMap<>();
    private static HashMap<String, CustomMaterialType> customMaterialTypes = new HashMap<>();

    public static void init(){
        for(Material material : Material.values()){
            materialTypes.put(material.name(), new MaterialType(material.name(),material));
        }
        File customMaterialsDir = FileUtils.getFolder(FileUtils.PATH_MATERIALS_CUSTOM);
        if(!customMaterialsDir.exists())
            customMaterialsDir.mkdirs();

        YamlParserLoader customItems = new YamlParserLoader()
                .addDefault("name","AUTO_GENEREATED")
                .addDefault("displayname","displayname")
                .addDefault("base","DIRT")
                .addDefault("custom_model_id",0);

        for(File file : customMaterialsDir.listFiles()){
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if(!customItems.verifyAllPathsAreThere(fc)){
                customItems.addDefaultValues(fc,file);
            }
            customMaterialTypes.put(fc.getString("name"),new CustomMaterialType(fc.getString("name"),fc.getString("displayname"),Material.matchMaterial(fc.getString("base")),fc.getInt("custom_model_id")));
        }


        File groupMaterialsDir = FileUtils.getFolder(FileUtils.PATH_MATERIALS_GROUP);
        if(!groupMaterialsDir.exists())
            groupMaterialsDir.mkdirs();

        YamlParserLoader groupItems = new YamlParserLoader()
                .addDefault("name","AUTO_GENEREATED")
                .addDefault("group", Arrays.asList("DIRT","GRASS"));

        for(File file : groupMaterialsDir.listFiles()){
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if(!groupItems.verifyAllPathsAreThere(fc)){
                groupItems.addDefaultValues(fc,file);
            }
            groupMaterialTypes.put(fc.getString("name"),new GroupMaterialType(fc.getString("name"),toMaterialTypesList(fc.getStringList("group"))));
        }
    }

    private Material base;
    private String name;

    public MaterialType(String name, Material base){
        this.name= name;
        this.base=base;
    }

    public static MaterialType getMaterialType(ItemStack itemInMainHand) {
        if(itemInMainHand==null)
            return MaterialType.getMaterialType("AIR");
        if(itemInMainHand.getItemMeta().hasCustomModelData()){
            for(MaterialType mt : customMaterialTypes.values()){
                if(mt instanceof CustomMaterialType && mt.base==itemInMainHand.getType()&&((CustomMaterialType) mt).getId()==itemInMainHand.getItemMeta().getCustomModelData()){
                    return mt;
                }
            }
        }
        for(MaterialType mt : materialTypes.values()){
            if(mt.base==itemInMainHand.getType()){
                return mt;
            }
        }
        return null;
    }

    public Material getBase() {
        return base;
    }

    public String getName() {
        return name;
    }

    public static List<MaterialType> toMaterialTypesList(List<String> names){
        List<MaterialType> materialTypes1 = new LinkedList<>();
        for(String s  : names){
            materialTypes1.add(getMaterialType(s));
        }
        return materialTypes1;
    }
    public static MaterialType getMaterialType(String name){
        if(groupMaterialTypes.containsKey(name))
            return groupMaterialTypes.get(name);
        if(customMaterialTypes.containsKey(name))
            return customMaterialTypes.get(name);
        return materialTypes.get(name);
    }

    public ItemStack toItemStack(){
        return new ItemStack(base);
    }
}
