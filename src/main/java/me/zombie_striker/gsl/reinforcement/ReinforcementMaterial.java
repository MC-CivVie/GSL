package me.zombie_striker.gsl.reinforcement;

import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.materials.MaterialType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ReinforcementMaterial {

    private static final List<ReinforcementMaterial> rm = new LinkedList<>();

    public static void init() {
        File customMaterialsDir = FileUtils.getFolder(FileUtils.PATH_REINFORCEMENT_TYPES);
        if (!customMaterialsDir.exists())
            customMaterialsDir.mkdirs();

        YamlParserLoader customItems = new YamlParserLoader()
                .addDefault("materialtype", "DIRT")
                .addDefault("durability", 0);

        for (File file : customMaterialsDir.listFiles()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if (!customItems.verifyAllPathsAreThere(fc)) {
                customItems.addDefaultValues(fc, file);
            }
            rm.add(new ReinforcementMaterial(MaterialType.getMaterialType(fc.getString("materialtype")),fc.getInt("durability")));
        }
    }

    public static ReinforcementMaterial getReinforcementMaterialData(MaterialType type) {
        for(ReinforcementMaterial r : rm){
            if(r.getType()==type){
                return r;
            }
        }
        return null;
    }

    private MaterialType type;
    private int durability;

    public ReinforcementMaterial(MaterialType type, int durability){
        this.type = type;
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public MaterialType getType() {
        return type;
    }
}
