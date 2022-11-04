package me.zombie_striker.gsl.megabuilds.interact;

import me.zombie_striker.gsl.megabuilds.MegaBuild;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public abstract class InteractAction {

    private static List<InteractAction> actionList = new LinkedList<>();

    public static void init(){
        registerAction(new OpenCraftingBenchGUI());
        registerAction(new TurnOnEnginAction());

    }

    public static InteractAction getAction(String string) {
        for(InteractAction ia : actionList){
            if(ia.getActionName().equalsIgnoreCase(string))
                return ia;
        }
        return null;
    }

    public static void registerAction(InteractAction ia){
        actionList.add(ia);
    }
    public abstract boolean onInteract(Player player, Block block, MegaBuild build, boolean rightclick);
    public abstract String getActionName();
}
