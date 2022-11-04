package me.zombie_striker.gsl.crops;

import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.world.GSLBiomeList;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CropType {


    private static List<CropType> croptype = new LinkedList<>();


    public static void init() {
        YamlParserLoader parserLoader = new YamlParserLoader()
                .addDefault("material", "WHEAT")
                .addDefault("growtype", GrowType.DEFAULT.name())
                .addDefault("growtime", 1.0)
                .addDefault("requiressun", true)
                .addDefault("soilmaterial", Material.FARMLAND.name())
                .addDefault("growthmodifiers.frozen", 4.0)
                .addDefault("growthmodifiers.cold", 4.0)
                .addDefault("growthmodifiers.hot", 2.0)
                .addDefault("growthmodifiers.temperate", 1.0)
                .addDefault("growthmodifiers.ocean", -1.0);


        File customMaterialsDir = FileUtils.getFolder(FileUtils.PATH_CROPS);
        if (!customMaterialsDir.exists())
            customMaterialsDir.mkdirs();

        for (File file : customMaterialsDir.listFiles()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if (!parserLoader.verifyAllPathsAreThere(fc)) {
                parserLoader.addDefaultValues(fc, file);
            }
            Material material = Material.matchMaterial(fc.getString("material"));
            Material soil = Material.matchMaterial(fc.getString("soilmaterial"));
            GrowType growtype = GrowType.valueOf(fc.getString("growtype"));
            boolean requiressun = fc.getBoolean("requiressun");
            double growtime = fc.getDouble("growtime");

            CropType cropType = new CropType(material, growtype,requiressun,soil,growtime);

            for (String key : fc.getConfigurationSection("growthmodifiers").getKeys(false)) {
                double value = fc.getDouble("growthmodifiers." + key);
                cropType.getGrowthModiferByBiome().put(GSLBiomeList.getBiomeByName(key), value);
            }
            croptype.add(cropType);
        }
    }

    public static CropType getCropTypeByMaterial(Material material){
        for(CropType t : croptype){
            if(t.getBlockmaterial()==material)
                return t;
        }
        return null;
    }


    private Material blockmaterial;
    private GrowType growType;
    private boolean requiresSunlight;
    private Material soilBoosters;
    private double defaultWaitingTime;

    private HashMap<GSLBiomeList, Double> growthModiferByBiome = new HashMap<>();

    public CropType(Material blockmaterial, GrowType growType, boolean requiresSunlight, Material soilType,double defaultWaitingTime) {
        this.blockmaterial = blockmaterial;
        this.growType = growType;
        this.defaultWaitingTime = defaultWaitingTime;
        this.requiresSunlight = requiresSunlight;
        this.soilBoosters = soilType;
    }

    public GrowType getGrowType() {
        return growType;
    }

    public HashMap<GSLBiomeList, Double> getGrowthModiferByBiome() {
        return growthModiferByBiome;
    }

    public Material getBlockmaterial() {
        return blockmaterial;
    }

    public boolean requiresSunlight() {
        return requiresSunlight;
    }

    public double getDefaultWaitingTime() {
        return defaultWaitingTime;
    }

    public static List<CropType> getCroptype() {
        return croptype;
    }

    public Material getSoilBoosters() {
        return soilBoosters;
    }
}
