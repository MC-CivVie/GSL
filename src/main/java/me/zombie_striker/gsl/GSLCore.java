package me.zombie_striker.gsl;

import org.bukkit.plugin.java.JavaPlugin;

public final class GSLCore extends JavaPlugin {

    @Override
    public void onEnable() {
        new GSL(this).init();
    }

    @Override
    public void onDisable() {

    }
}
