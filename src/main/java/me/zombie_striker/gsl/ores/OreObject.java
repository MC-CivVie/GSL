package me.zombie_striker.gsl.ores;

import me.zombie_striker.gsl.files.FileUtils;
import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.materials.MaterialType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class OreObject {


    public static SimplexOctaveGenerator TOUGHNESS = new SimplexOctaveGenerator(1354235,8);
    public static SimplexOctaveGenerator ROUGHNESS = new SimplexOctaveGenerator(1554235,32);
    public static SimplexOctaveGenerator METALIC = new SimplexOctaveGenerator(1356235,16);

    static{
        TOUGHNESS.setScale(0.01);
        ROUGHNESS.setScale(0.02);
        METALIC.setScale(0.04);
    }

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
                .addDefault("blocktype","DIRT")
                .addDefault("replacetype","STONE");

        File f = FileUtils.getFolder(FileUtils.PATH_ORES);
        if(!f.exists())
            f.mkdirs();

        for(File file : f.listFiles()){
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if(parserLoader.verifyAllPathsAreThere(fc))
                parserLoader.addDefaultValues(fc,file);

            OreObject ore = new OreObject(fc.getInt("rarity"),fc.getDouble("toughness"),fc.getDouble("roughness"),fc.getDouble("metallic"),Material.matchMaterial(fc.getString("blocktype")),Material.matchMaterial(fc.getString("replacetype")));
            oreObjects.add(ore);
        }
    }



    public static OreObject getRandomOreForType(Material replaceType, int x, int y, int z){
        int randx = (int) ((Math.random()*16)-8);
        int randy = (int) ((Math.random()*16)-8);
        int randz = (int) ((Math.random()*16)-8);

        if(Math.random()<0.015) {
            OreObject closest = null;
            double distanceClose = Integer.MAX_VALUE;
            for (OreObject oreObject : oreObjects) {
                if(oreObject.getReplaceType()==replaceType) {
                    double s1 = OreObject.ROUGHNESS.noise(x + randx, y + randy, z + randz, randx + randy + randz)-oreObject.simplex1Center;
                    double s2 = OreObject.TOUGHNESS.noise(x + randx, y + randy, z + randz, randx + randy + randz)-oreObject.simplex2Center;
                    double s3 = OreObject.METALIC.noise(x + randx, y + randy, z + randz, randx + randy + randz)-oreObject.simplex3Center;

                    double dist = (s1 * s1) + (s2 * s2) + (s3 * s3);
                    if (dist < distanceClose) {
                        distanceClose = dist;
                        closest = oreObject;
                    }
                }
            }
            return closest;
        }
        return null;
    }

    private double rarity;

    private double simplex1Center;
    private double simplex2Center;
    private double simplex3Center;

    private Material oreBlock;
    private Material replaceType;

    public OreObject(double rarity, double s1c, double s2c, double s3c, Material oreBlock, Material replacetype){
        this.rarity = rarity;
        this.simplex1Center = s1c;
        this.simplex2Center = s2c;
        this.simplex3Center = s3c;
        this.oreBlock = oreBlock;
        this.replaceType = replacetype;
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

    public Material getReplaceType() {
        return replaceType;
    }
}
