package me.zombie_striker.gsl.megabuilds.interact;

import me.zombie_striker.gsl.megabuilds.MegaBuild;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TurnOnEnginAction extends InteractAction {
    @Override
    public boolean onInteract(Player player, Block block, MegaBuild build, boolean rightclick) {
        if(!rightclick){
            build.setActive(true);
        }
        return false;
    }

    @Override
    public String getActionName() {
        return "engine_activation";
    }
}
