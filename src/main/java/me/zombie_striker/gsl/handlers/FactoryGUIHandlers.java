package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.megabuilds.MegaBuild;
import me.zombie_striker.gsl.recipes.FactoryRecipe;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.ItemUtil;
import me.zombie_striker.gsl.utils.guis.GUI;
import me.zombie_striker.gsl.utils.guis.GUIAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FactoryGUIHandlers {

    public static GUI createGUIManageRecipeFor(MegaBuild build){
        GUI gui = new GUI(Bukkit.createInventory(null,54,build.getType().getDisplayname()));

        int slot = 0;
        for(FactoryRecipe factoryRecipe : FactoryRecipe.getFactoryRecipeList()){
            if(factoryRecipe.getGroup().equals(build.getType().getGroup())){
                gui.setIcon(slot, ItemUtil.prepareFactoryIcon(factoryRecipe), new GUIAction() {
                    @Override
                    public void click(int slot, Player player, GUI gui) {
                        build.setRecipe(factoryRecipe);
                        player.sendMessage(new ComponentBuilder("Setting factory recipe to "+factoryRecipe.getDisplayname()+".",ComponentBuilder.GREEN).build());
                    }
                });
                slot++;
            }
        }

        return gui;
    }
}
