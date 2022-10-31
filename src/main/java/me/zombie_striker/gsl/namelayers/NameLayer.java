package me.zombie_striker.gsl.namelayers;

import me.zombie_striker.gsl.files.FileUtils;
import me.zombie_striker.gsl.files.YamlParserLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.units.qual.N;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
    public void addMemberRank(UUID uuid, byte rank){
        this.memberranks.put(uuid,rank);
    }
    public void addMerger(UUID uuid){
        this.mergers.add(uuid);
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
