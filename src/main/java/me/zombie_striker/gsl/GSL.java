package me.zombie_striker.gsl;

import me.zombie_striker.gsl.entities.EntityData;
import me.zombie_striker.gsl.events.*;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.megabuilds.MegaBuildType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.ores.OreObject;
import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.utils.InternalFileUtil;
import me.zombie_striker.gsl.world.GSLWorld;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;

public class GSL {

    private static GSLCore core;
    private static GSL api;

    public static GSL getApi() {
        return api;
    }

    public static GSLCore getCore() {
        return core;
    }

    public GSL(GSLCore gslCore) {
        core = gslCore;
        api = this;
    }

    public void init() {
        copyDataFiles();

        GSLWorld.init();
        MaterialType.init();
        OreObject.init();
        NameLayer.init();
        MegaBuildType.init();
        EntityData.init();

        registerListeners();
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new OreEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ChunkLoadEvents(), core);
        Bukkit.getPluginManager().registerEvents(new NamelayerEvents(), core);
        Bukkit.getPluginManager().registerEvents(new JukeEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ReinforceEvents(), core);
        Bukkit.getPluginManager().registerEvents(new EntityEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ExpEvents(), core);
    }
    public void copyDataFiles(){

        try {
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), "entities"), InternalFileUtil.getPathsToInternalFiles("entities"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), "materials/custom"), InternalFileUtil.getPathsToInternalFiles("materials.custom"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), "materials/groups"), InternalFileUtil.getPathsToInternalFiles("materials.groups"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), "ores"), InternalFileUtil.getPathsToInternalFiles("ores"), false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
