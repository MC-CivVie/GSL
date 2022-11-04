package me.zombie_striker.gsl.utils;

import me.zombie_striker.gsl.materials.CustomMaterialType;
import me.zombie_striker.gsl.materials.GroupMaterialType;
import me.zombie_striker.gsl.materials.MaterialType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    /**
     * Removes onme of a specified Material Type from an inventory. Works with groups of items as well.
     * @param type
     * @param inventory
     */
    public static void removeOneOf(MaterialType type, Inventory inventory) {
        if (type instanceof GroupMaterialType) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    if (((GroupMaterialType) type).getMaterials().contains(MaterialType.getMaterialType(is))) {
                        is.setAmount(is.getAmount() - 1);
                        if (is.getAmount() <= 0)
                            is = null;
                        inventory.setItem(i, is);
                        return;
                    }
                }
            }
        } else if (type instanceof CustomMaterialType) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    if (type.getBase() == is.getType())
                        if (is.getItemMeta().hasCustomModelData())
                            if (((CustomMaterialType) type).getId() == is.getItemMeta().getCustomModelData()) {
                                is.setAmount(is.getAmount() - 1);
                                if (is.getAmount() <= 0)
                                    is = null;
                                inventory.setItem(i, is);
                                return;
                            }
                }
            }
        } else {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    if (type.getBase() == is.getType()) {
                        is.setAmount(is.getAmount() - 1);
                        if (is.getAmount() <= 0)
                            is = null;
                        inventory.setItem(i, is);
                        return;
                    }
                }
            }
        }
    }
}
