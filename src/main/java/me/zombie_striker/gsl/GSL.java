package me.zombie_striker.gsl;

import me.zombie_striker.gsl.events.*;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.megabuilds.MegaBuildType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.ores.OreObject;
import me.zombie_striker.gsl.world.GSLWorld;
import org.bukkit.Bukkit;

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
        GSLWorld.init();
        MaterialType.init();
        OreObject.init();
        NameLayer.init();
        MegaBuildType.init();

        registerListeners();
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new OreEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ChunkLoadEvents(), core);
        Bukkit.getPluginManager().registerEvents(new NamelayerEvents(), core);
        Bukkit.getPluginManager().registerEvents(new JukeEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ReinforceEvents(), core);
    }
}
