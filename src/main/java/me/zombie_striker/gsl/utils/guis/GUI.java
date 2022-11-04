package me.zombie_striker.gsl.utils.guis;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI {

    private Inventory inventory;

    private GUIAction[] actions;

    public GUI(Inventory inventory) {
        this.inventory = inventory;
        actions = new GUIAction[inventory.getSize()];
        GUIUtil.register(this);
    }

    public void setIcon(int slot, ItemStack material, GUIAction action) {
        inventory.setItem(slot, material);
        actions[slot] = action;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public GUIAction[] getActions() {
        return actions;
    }
}
