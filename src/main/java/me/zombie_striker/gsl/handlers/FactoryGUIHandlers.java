package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.megabuilds.MegaBuild;
import me.zombie_striker.gsl.utils.guis.GUI;
import org.bukkit.Bukkit;

public class FactoryGUIHandlers {

    public static GUI createGUIManageRecipeFor(MegaBuild build){
        GUI gui = new GUI(Bukkit.createInventory(null,54,build.getType().getDisplayname()));





        return gui;
    }
}
