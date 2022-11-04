package me.zombie_striker.gsl.megabuilds.interact;

import me.zombie_striker.gsl.handlers.FactoryGUIHandlers;
import me.zombie_striker.gsl.megabuilds.MegaBuild;
import me.zombie_striker.gsl.utils.guis.GUI;
import me.zombie_striker.gsl.world.GSLWorld;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class OpenCraftingBenchGUI extends InteractAction {
    @Override
    public boolean onInteract(Player player, Block block,MegaBuild build, boolean rightclick) {
        if(rightclick){
            GUI gui = FactoryGUIHandlers.createGUIManageRecipeFor(build);
            player.openInventory(gui.getInventory());
            return true;
        }
        return false;
    }

    @Override
    public String getActionName() {
        return "factory_open";
    }
}
