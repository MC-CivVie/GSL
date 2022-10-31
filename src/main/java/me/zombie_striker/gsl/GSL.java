package me.zombie_striker.gsl;

import me.zombie_striker.gsl.events.ChunkLoadEvents;
import me.zombie_striker.gsl.events.OreEvents;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.ores.OreObject;
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
        MaterialType.init();
        OreObject.init();
        NameLayer.init();


        registerListeners();
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new OreEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ChunkLoadEvents(), core);
    }
}
