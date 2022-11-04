package me.zombie_striker.gsl.megabuilds;

import me.zombie_striker.gsl.GSL;
import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.megabuilds.interact.InteractAction;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MegaBuildType {

    private static final List<MegaBuildType> buildtypes = new LinkedList<>();

    public static void init() {


        YamlParserLoader parserLoader = new YamlParserLoader()
                .addDefault("name", "NAME")
                .addDefault("factory", "type")
                .addDefault("displayname", "Name")
                .addDefault("axis", BuildAxis.UP.name())
                .addDefault("xoff", 0)
                .addDefault("yoff", 0)
                .addDefault("zoff", 0)
                .addDefault("x_size", 1)
                .addDefault("y_size", 1)
                .addDefault("z_size", 1)
                .addOptional("materials.0x0x0", "CRAFTING_TABLE")
                .addOptional("actions.0x0x0", "factory_open");


        File dir = FileUtils.getFolder(FileUtils.PATH_MEGASTRUCTURETYPES);
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            FileConfiguration yml = YamlConfiguration.loadConfiguration(f);
            if (!parserLoader.verifyAllPathsAreThere(yml))
                parserLoader.addDefaultValues(yml, f);

            String name = yml.getString("name");
            String group = yml.getString("factory");
            String display = yml.getString("displayname");
            BuildAxis buildAxis1 = BuildAxis.valueOf(yml.getString("axis"));
            int xoff = yml.getInt("xoff");
            int yoff = yml.getInt("yoff");
            int zoff = yml.getInt("zoff");

            int x_size = yml.getInt("x_size");
            int y_size = yml.getInt("y_size");
            int z_size = yml.getInt("z_size");

            Material[][][] types = new Material[x_size][y_size][z_size];

            if (yml.contains("materials")) {
                for (String keyx : yml.getConfigurationSection("materials").getKeys(false)) {
                    String[] parts = keyx.split("x");
                    if (parts.length < 3) {
                        GSL.getCore().getLogger().info("Failed to load " + name + " part " + keyx);
                        continue;
                    }
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);


                    types[x][y][z] = Material.matchMaterial(yml.getString("materials." + keyx));
                }
            }
            InteractAction[][][] actions = new InteractAction[x_size][y_size][z_size];
            if (yml.contains("actions")) {
                for (String keyx : yml.getConfigurationSection("actions").getKeys(false)) {
                    String[] parts = keyx.split("x");
                    if (parts.length < 3) {
                        GSL.getCore().getLogger().info("Failed to load " + name + " action " + keyx);
                        continue;
                    }
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);

                    InteractAction action = InteractAction.getAction(yml.getString("actions." + keyx));


                    if (action != null) {
                        actions[x][y][z] = action;
                    } else {
                        GSL.getCore().getLogger().info("Failed to find action " + yml.getString("actions." + keyx) + " for " + keyx);
                    }
                }
            }
            buildtypes.add(new MegaBuildType(name, display, group, types, actions, xoff, yoff, zoff, buildAxis1));
        }
    }

    private Material[][][] types;
    private InteractAction[][][] actions;
    private String name;
    private String displayname;
    private String group;
    private BuildAxis buildAxis;
    private int offsetX;
    private int offsetY;
    private int offsetZ;

    public MegaBuildType(String name, String displayname, String group, Material[][][] types, InteractAction[][][] actions, int offsetX, int offsetY, int offsetZ, BuildAxis axis) {
        this.name = name;
        this.types = types;
        this.group = group;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.displayname = displayname;
        this.buildAxis = axis;
        this.actions = actions;
    }

    public boolean isValidStructure(Location center) {
        for (int x = 0; x < types.length; x++) {
            for (int y = 0; y < types[x].length; y++) {
                for (int z = 0; z < types[x][y].length; z++) {
                    Location newloc = new Location(center.getWorld(), center.getBlockX() - offsetX + x, center.getBlockY() - offsetY + y, center.getBlockZ() - offsetZ + z);
                    if (types[x][y][z] != null && types[x][y][z] != newloc.getBlock().getType()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getGroup() {
        return group;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getName() {
        return name;
    }

    public BuildAxis getBuildAxis() {
        return buildAxis;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public InteractAction[][][] getActions() {
        return actions;
    }

    public static List<MegaBuildType> getBuildtypes() {
        return buildtypes;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public Material[][][] getTypes() {
        return types;
    }
}
