package me.zombie_striker.gsl.ores;

import me.zombie_striker.gsl.files.FileUtils;
import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.materials.MaterialType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class OreObject {


    public static final List<OreObject> oreObjects = new LinkedList<>();

    public static List<OreObject> getOreObjects() {
        return oreObjects;
    }

    public static void init(){
        YamlParserLoader parserLoader = new YamlParserLoader()
                .addDefault("rarity",100)
                .addDefault("toughness",1)
                .addDefault("roughness",1)
                .addDefault("metallic",1)
                .addDefault("blocktype","DIRT");

        File f = FileUtils.getFolder(FileUtils.PATH_ORES);
        if(!f.exists())
            f.mkdirs();

        for(File file : f.listFiles()){
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if(parserLoader.verifyAllPathsAreThere(fc))
                parserLoader.addDefaultValues(fc,file);

            OreObject ore = new OreObject(fc.getInt("rarity"),fc.getDouble("toughness"),fc.getDouble("roughness"),fc.getDouble("metallic"),Material.matchMaterial(fc.getString("blocktype")));
            oreObjects.add(ore);
        }
    }

    private double rarity;

    private double simplex1Center;
    private double simplex2Center;
    private double simplex3Center;

    private Material oreBlock;

    public OreObject(double rarity, double s1c, double s2c, double s3c, Material oreBlock){
        this.rarity = rarity;
        this.simplex1Center = s1c;
        this.simplex2Center = s2c;
        this.simplex3Center = s3c;
        this.oreBlock = oreBlock;
    }

    public double getRarity() {
        return rarity;
    }

    public double getSimplex1Center() {
        return simplex1Center;
    }

    public double getSimplex2Center() {
        return simplex2Center;
    }

    public double getSimplex3Center() {
        return simplex3Center;
    }

    public Material getOreBlock() {
        return oreBlock;
    }

}
