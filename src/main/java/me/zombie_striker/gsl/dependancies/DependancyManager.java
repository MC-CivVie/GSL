package me.zombie_striker.gsl.dependancies;

import org.bukkit.Bukkit;

public class DependancyManager {

    private static boolean terraInstalled=false;

    public static void init(){
        if(Bukkit.getPluginManager().isPluginEnabled("Terra")){
            terraInstalled= true;
        }
    }

    public static boolean hasTerra(){
        return terraInstalled;
    }

}
