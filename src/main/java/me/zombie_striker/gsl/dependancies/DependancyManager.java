package me.zombie_striker.gsl.dependancies;

import org.bukkit.Bukkit;

public class DependancyManager {

    private static boolean terraInstalled=false;
    private static boolean noteblockAPIInstalled = false;

    public static void init(){
        if(Bukkit.getPluginManager().isPluginEnabled("Terra")){
            terraInstalled= true;
        }
        if(Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")){
            noteblockAPIInstalled= true;
        }
    }

    public static boolean hasTerra(){
        return terraInstalled;
    }
    public static boolean hasNoteblockAPI(){
        return noteblockAPIInstalled;
    }

}
