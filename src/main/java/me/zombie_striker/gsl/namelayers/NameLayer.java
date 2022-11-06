package me.zombie_striker.gsl.namelayers;

import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.files.YamlParserLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NameLayer {

    private static final List<NameLayer> nameLayers = new LinkedList<>();


    public static void init(){

        YamlParserLoader parserLoader = new YamlParserLoader()
                .addDefault("name","NAME")
                .addDefault("uuid",UUID.randomUUID().toString());



        File dir = FileUtils.getFolder(FileUtils.PATH_NAMELAYERS);
        if(!dir.exists())
            dir.mkdirs();
        for(File f : dir.listFiles()) {
            FileConfiguration yml = YamlConfiguration.loadConfiguration(f);
            if(parserLoader.verifyAllPathsAreThere(yml))
                parserLoader.addDefaultValues(yml,f);

            NameLayer nl = new NameLayer(yml.getString("name"), UUID.fromString(yml.getString("uuid")));
            if(yml.contains("members")){
                for(String key : yml.getConfigurationSection("members").getKeys(false)){
                    nl.addMemberRank(UUID.fromString(key), (byte) yml.getInt("members."+key));
                }
            }
            if(yml.contains("mergers")){
                for(String s : yml.getStringList("mergers")){
                    nl.addMerger(UUID.fromString(s));
                }
            }
            nameLayers.add(nl);
        }
    }
    public static void saveNamelayers(){
        File dir = FileUtils.getFolder(FileUtils.PATH_NAMELAYERS);
        for(NameLayer nameLayer: nameLayers){
            File file = new File(dir,nameLayer.getUuid()+".yml");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            fc.set("members",null);
            fc.set("name",nameLayer.getName());
            fc.set("uuid",nameLayer.getUuid().toString());
            for(Map.Entry<UUID, Byte> e : nameLayer.getMemberranks().entrySet())
            fc.set("members."+e.getKey(),e.getValue());

            List<String> mergers = new LinkedList<>();
            for(UUID m : nameLayer.getMergers()){
                mergers.add(m.toString());
            }
            fc.set("mergers",mergers);
            try {
                fc.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<NameLayer> getNameLayers() {
        return nameLayers;
    }
    public static NameLayer getNameLayer(UUID uuid){
        for(NameLayer nl : getNameLayers()){
            if(nl.getUuid().equals(uuid))
                return nl;
            if(nl.getMergers().contains(uuid))
                return nl;
        }
        return null;
    }
    public static NameLayer getNameLayer(String name){
        for(NameLayer nl : getNameLayers()){
            if(nl.getName().equals(name))
                return nl;
        }
        return null;
    }

    private final UUID uuid;
    private String name;
    private HashMap<UUID,Byte> memberranks = new HashMap<>();
    private List<UUID> mergers = new LinkedList<>();


    public NameLayer(String name, UUID uuid){
        this.name = name;
        this.uuid = uuid;
    }

    public static void register(NameLayer nameLayer) {
        nameLayers.add(nameLayer);
    }

    public void addMemberRank(UUID uuid, byte rank){
        this.memberranks.put(uuid,rank);
    }
    public void addMerger(UUID uuid){
        this.mergers.add(uuid);
    }

    public static void disband(NameLayer nameLayer){
        nameLayers.remove(nameLayer);
    }

    public String getName() {
        return name;
    }

    public HashMap<UUID, Byte> getMemberranks() {
        return memberranks;
    }

    public List<UUID> getMergers() {
        return mergers;
    }

    public UUID getUuid() {
        return uuid;
    }
}
