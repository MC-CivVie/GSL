package me.zombie_striker.gsl.world;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GSLChunk {
    public static final int BLOCK_Y_OFFSET = -64;
    private static List<GSLChunk> chunks = new LinkedList<>();

    public static GSLChunk getGSLChunk(Chunk chunk) {
        for (GSLChunk gslc : chunks) {
            if (gslc.getXc() == chunk.getX() && gslc.getZc() == chunk.getZ())
                return gslc;
        }
        return new GSLChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }


    private GSLCube[] cubes;
    private String worldname;
    private int xc;
    private int zc;

    public GSLChunk(String worldname, int xc, int zc) {
        cubes = new GSLCube[(320 + 64) / 16];
        this.xc = xc;
        this.zc = zc;
        this.worldname = worldname;
    }

    public static void register(GSLChunk gsl) {
        chunks.add(gsl);
    }

    public static void unregister(GSLChunk gslChunk) {
        chunks.remove(gslChunk);
    }

    public GSLCube[] getCubes() {
        return cubes;
    }

    public int getXc() {
        return xc;
    }

    public int getZc() {
        return zc;
    }

    public static GSLChunk load(Chunk chunk) {

        File dir = FileUtils.getFolder(FileUtils.PATH_CHUNKS);
        if (!dir.exists())
            dir.mkdirs();

        File world = new File(dir, chunk.getWorld().getName());
        if (!world.exists())
            world.mkdirs();

        GSLChunk gslChunk = new GSLChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());

        File file = new File(world, "x" + chunk.getX() + "z" + chunk.getZ() + ".yml");
        if (file.exists()) {
            FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
            for (int i = 0; i < gslChunk.cubes.length; i++) {
                if (yml.contains("cube." + i)) {
                    GSLCube gslCube = new GSLCube();
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                if (yml.contains("cube." + i + "." + x + "." + y + "." + z + ".dur")) {
                                    int dur = yml.getInt("cube." + i + "." + x + "." + y + "." + z + ".dur");
                                    String namelayer = yml.getString("cube." + i + "." + x + "." + y + "." + z + ".nl");
                                    String reinforcedby = yml.getString("cube." + i + "." + x + "." + y + "." + z + ".rb");
                                    gslCube.getDurability()[x][y][z] = dur;
                                    gslCube.getNamelayers()[x][y][z] = NameLayer.getNameLayer(UUID.fromString(namelayer));
                                    gslCube.getReinforcedBy()[x][y][z] = MaterialType.getMaterialType(reinforcedby);
                                }
                                if (yml.contains("cube." + i + "." + x + "." + y + "." + z + ".planttime")) {
                                    long planttime = yml.getInt("cube." + i + "." + x + "." + y + "." + z + ".planttime");
                                    gslCube.getPlantDate()[x][y][z] = planttime;
                                }
                                if (yml.contains("cube." + i + "." + x + "." + y + "." + z + ".pl")) {
                                    boolean placed = yml.getBoolean("cube." + i + "." + x + "." + y + "." + z + ".pl");
                                    gslCube.getPlaced()[x][y][z] = placed;
                                }
                            }
                        }
                    }
                    gslChunk.cubes[i] = gslCube;
                }
            }
        }
        return gslChunk;
    }

    public void save() {
        File dir = FileUtils.getFolder(FileUtils.PATH_CHUNKS);
        if (!dir.exists())
            dir.mkdirs();

        File world = new File(dir, worldname);
        if (!world.exists())
            world.mkdirs();

        File file = new File(world, "x" + xc + "z" + zc + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
        for (int i = 0; i < cubes.length; i++) {
            GSLCube cube = cubes[i];
            if (cube != null) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            if (cube.getDurability()[x][y][z] > 0) {
                                yml.set("cube." + i + "." + x + "." + y + "." + z + ".dur", cube.getDurability()[x][y][z]);
                                yml.set("cube." + i + "." + x + "." + y + "." + z + ".nl", cube.getNamelayers()[x][y][z].getUuid().toString());
                                yml.set("cube." + i + "." + x + "." + y + "." + z + ".rb", cube.getReinforcedBy()[x][y][z].getName());
                            }
                            if (cube.getPlantDate()[x][y][z] > 0)
                                yml.set("cube." + i + "." + x + "." + y + "." + z + ".planttime", cube.getPlantDate()[x][y][z]);
                            if (cube.getPlaced()[x][y][z])
                                yml.set("cube." + i + "." + x + "." + y + "." + z + ".pl", cube.getPlaced()[x][y][z]);
                        }
                    }
                }
            }
        }
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
