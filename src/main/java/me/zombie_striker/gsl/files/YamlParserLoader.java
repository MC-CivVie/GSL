package me.zombie_striker.gsl.files;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlParserLoader {

    private HashMap<String,Object> defaultValues = new HashMap<>();
    private HashMap<String,Object> optionalValues = new HashMap<>();

    public YamlParserLoader(){

    }
    public YamlParserLoader addDefault(String path, Object defaultValue){
        this.defaultValues.put(path,defaultValue);
        return this;
    }

    public YamlParserLoader addOptional(String path, Object defaultValue){
        this.optionalValues.put(path,defaultValue);
        return this;
    }

    public boolean verifyAllPathsAreThere(FileConfiguration yml){
        for(String e : defaultValues.keySet()){
            if(!yml.contains(e)){
                return false;
            }
        }
        return true;
    }
    public void addDefaultValues(FileConfiguration yml, File file){
        for(Map.Entry<String, Object> e : defaultValues.entrySet()){
            if(!yml.contains(e.getKey())){
                yml.set(e.getKey(),e.getValue());
            }
        }
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, Object> getDefaultValues() {
        return defaultValues;
    }

    public HashMap<String, Object> getOptionalValues() {
        return optionalValues;
    }
}
