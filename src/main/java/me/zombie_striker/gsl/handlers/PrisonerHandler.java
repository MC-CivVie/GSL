package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.utils.StringUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PrisonerHandler {

    private static HashMap<UUID, Location> prisoners = new HashMap<>();

    public static void init(){
        File file = FileUtils.getFolder(FileUtils.PATH_PRISONERS_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlParserLoader parserLoader = new YamlParserLoader().addOptional("prisoners.test.location","world,0,0,0");

        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
        if(!parserLoader.verifyAllPathsAreThere(yml))
            parserLoader.addDefaultValues(yml,file);

        if(yml.contains("prisoners")){
            for(String uuids : yml.getConfigurationSection("prisoners").getKeys(false)){
                UUID uuid = UUID.fromString(uuids);
                Location location = StringUtil.getLocationFromString(yml.getString("prisoners."+uuids+".location"));
                prisoners.put(uuid,location);
            }
        }
    }

    public static void imprison(UUID uuid, Location location){
        prisoners.put(uuid,location);
    }
    public static void free(UUID uuid){
        prisoners.remove(uuid);
    }

    public static void save(){

        File file = FileUtils.getFolder(FileUtils.PATH_PRISONERS_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
        for(Map.Entry<UUID, Location> e : prisoners.entrySet()){
           yml.set("prisoners."+e.getKey().toString()+".location",StringUtil.getStringLocation(e.getValue()));
        }
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPrisoner(UUID uuid){
        return prisoners.containsKey(uuid);
    }
    public static Location getPrisonLocation(UUID uuid){
        return prisoners.get(uuid);
    }

    public static HashMap<UUID, Location> getPrisoners() {
        return prisoners;
    }
}
