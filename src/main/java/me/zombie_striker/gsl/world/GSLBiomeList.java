package me.zombie_striker.gsl.world;

import me.zombie_striker.gsl.dependancies.DependancyManager;
import me.zombie_striker.gsl.dependancies.TerraManager;
import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GSLBiomeList {


    private static List<GSLBiomeList> biomeLists = new LinkedList<>();

    public static void init(World world) {
        YamlParserLoader parserLoader = new YamlParserLoader()
                .addDefault("groups.hot", Arrays.asList("DESERT", "SAVANNA", "SAVANNA_PLATEAU", "WINDSWEPT_SAVANNA", "BADLANDS", "WOODED_BADLANDS", "ERODED_BADLANDS"))
                .addDefault("groups.water", Arrays.asList("RIVER", "FROZEN_RIVER", "WARM_OCEAN", "LUKEWARM_OCEAN", "DEEP_LUKEWARM_OCEAN", "OCEAN", "DEEP_OCEAN", "COLD_OCEAN", "DEEP_COLD_OCEAN", "FROZEN_OCEAN", "DEEP_FROZEN_OCEAN"))
                .addDefault("groups.frozen", Arrays.asList("SNOWY_PLAINS", "ICE_SPIKES", "SNOWY_TAIGA", "SNOWY_BEACH", "GROVE", "SNOWY_SLOPES", "JAGGED_PEAKS", "FROZEN_PEAKS"))
                .addDefault("groups.cold", Arrays.asList("WINDSWEPT_HILLS", "WINDSWEPT_GRAVELLY", "WINDSWEPT_FOREST", "TAIGA", "OLD_GROWTH_PINE_TAIGA", "OLD_GROWTH_SPRUCE_TAIGA", "STONEY_SHORE"))
                .addDefault("groups.temperate", Arrays.asList("PLAINS", "SUNFLOWER_PLAINS", "FOREST", "FLOWER_FOREST", "BIRCH_FOREST", "OLD_GROWTH_BIRCH_FOREST", "DARK_FOREST", "SWAMP", "MANGROVE_SWAMP", "JUNGLE", "SPARSE_JUNGLE", "BAMBOO_JUNGLE", "BEACH", "MUSHROOM_FIELDS", "MEADOW", "STONY_PEAKS"));

        File f = FileUtils.getFolder(FileUtils.PATH_BIOMES_FILE);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileConfiguration yml = YamlConfiguration.loadConfiguration(f);
        if (parserLoader.verifyAllPathsAreThere(yml))
            parserLoader.addDefaultValues(yml, f);

        for(Biome biome : Biome.values()){
            biomeLists.add(new GSLBiomeList(biome.name(),biome));
        }
        for(String key : yml.getConfigurationSection("groups").getKeys(false)){
            List<Biome> biomes = new LinkedList<>();
            for(String s : yml.getStringList("groups."+key)){
                if(DependancyManager.hasTerra()){
                    biomes.add(TerraManager.getBiomeByName(world, s));
                }else{
                    biomes.add(Biome.valueOf(s));
                }
            }
            biomeLists.add(new GSLBiomeList(key,biomes));
        }
    }

    public static List<GSLBiomeList> getBiomeLists() {
        return biomeLists;
    }
    public static GSLBiomeList getBiomeByName(String name){
        for(GSLBiomeList list : biomeLists){
            if(list.getName().equals(name))
                return list;
        }
        return null;
    }

    private List<Biome> biomes = new LinkedList<>();
    private String name;

    public GSLBiomeList(String name, Biome... biomes) {
        for (Biome b : biomes) {
            this.biomes.add(b);
        }
        this.name = name;
    }
    public GSLBiomeList(String name, List<Biome> biomes) {
        for (Biome b : biomes) {
            this.biomes.add(b);
        }
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public List<Biome> getBiomes() {
        return biomes;
    }
}
