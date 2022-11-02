package me.zombie_striker.gsl.entities;

import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EntityData {

    private static final List<EntityData> entityData = new LinkedList<>();

    public static void init() {
        File customMaterialsDir = FileUtils.getFolder(FileUtils.PATH_ENTITY_DATA);
        if (!customMaterialsDir.exists())
            customMaterialsDir.mkdirs();

        YamlParserLoader customItems = new YamlParserLoader()
                .addDefault("entitytype", "AUTO_GENEREATED")
                .addDefault("displayname", "displayname")
                .addDefault("drops", Arrays.asList("DIRT"))
                .addDefault("naturalspawn", true)
                .addOptional("percentagedrops.DIRT",0.1)
                .addOptional("hostile",false);

        for (File file : customMaterialsDir.listFiles()) {
            FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
            if (!customItems.verifyAllPathsAreThere(fc)) {
                customItems.addDefaultValues(fc, file);
            }
            EntityData entityData1 = new EntityData(EntityType.valueOf(fc.getString("entitytype")), fc.getString("displayname"),fc.getBoolean("naturalspawn"));
            entityData1.setDropsPersistant(MaterialType.toMaterialTypesList(fc.getStringList("drops")));

            if(fc.contains("hostile")){
                entityData1.setHostileMob(fc.getBoolean("hostile"));
            }

            if(fc.contains("percentagedrops")){
                for(String key : fc.getConfigurationSection("percentagedrops").getKeys(false)){
                    double chance = fc.getDouble("percentagedrops."+key);
                    MaterialType type = MaterialType.getMaterialType(key);
                    entityData1.getPercentageDrop().put(type,chance);
                }
            }
        }
    }


    public static EntityData getEntityData(EntityType type) {
        for (EntityData e : entityData) {
            if (e.getEntityType() == type)
                return e;
        }
        return null;
    }

    private EntityType entityType;
    private String displayname;
    private boolean naturalSpawn;
    private boolean hostileMob = false;
    private List<MaterialType> dropsPersistant = new LinkedList<>();
    private HashMap<MaterialType, Double> percentageDrop = new HashMap<>();

    public EntityData(EntityType entityType, String displayname, boolean naturalSpawn) {
        this.entityType = entityType;
        this.displayname = displayname;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public List<MaterialType> getDropsPersistant() {
        return dropsPersistant;
    }

    public void setDropsPersistant(List<MaterialType> dropsPersistant) {
        this.dropsPersistant = dropsPersistant;
    }

    public String getDisplayname() {
        return displayname;
    }

    public HashMap<MaterialType, Double> getPercentageDrop() {
        return percentageDrop;
    }
    public boolean canNaturallySpawn(){
        return naturalSpawn;
    }

    public void setHostileMob(boolean hostileMob) {
        this.hostileMob = hostileMob;
    }

    public boolean isHostileMob() {
        return hostileMob;
    }

    public static List<EntityData> getEntityData() {
        return entityData;
    }
}
