package me.zombie_striker.gsl.utils;

import me.zombie_striker.gsl.materials.CustomMaterialType;
import me.zombie_striker.gsl.materials.GroupMaterialType;
import me.zombie_striker.gsl.materials.MaterialType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    /**
     * Removes one of a specified Material Type from an inventory. Works with groups of items as well.
     *
     * @param type
     * @param inventory
     */
    public static void removeAmount(MaterialType type, int amountRemove, Inventory inventory) {
        int amount = amountRemove;
        if (type instanceof GroupMaterialType) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    if (((GroupMaterialType) type).getMaterials().contains(MaterialType.getMaterialType(is))) {
                        int a = is.getAmount();
                        is.setAmount(is.getAmount() - amount);
                        amount-=a;
                        if(is.getAmount() == 0){
                            is = null;
                            inventory.setItem(i, is);
                            continue;
                        }else if (is.getAmount() <= 0) {
                            is = null;
                            inventory.setItem(i, is);
                        }else{
                            inventory.setItem(i, is);
                        }
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
                                int a = is.getAmount();
                                is.setAmount(is.getAmount() - amount);
                                amount-=a;
                                if(is.getAmount() == 0){
                                    is = null;
                                    inventory.setItem(i, is);
                                    continue;
                                }else if (is.getAmount() <= 0) {
                                    is = null;
                                    inventory.setItem(i, is);
                                }else{
                                    inventory.setItem(i, is);
                                }
                                return;
                            }
                }
            }
        } else {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    if (type.getBase() == is.getType()) {
                        int a = is.getAmount();
                        is.setAmount(is.getAmount() - amount);
                        amount-=a;
                        if(is.getAmount() == 0){
                            is = null;
                            inventory.setItem(i, is);
                            continue;
                        }else if (is.getAmount() <= 0) {
                            is = null;
                            inventory.setItem(i, is);
                        }else{
                            inventory.setItem(i, is);
                        }
                        return;
                    }
                }
            }
        }
    }

    public static boolean hasAtleast(MaterialType type,int amountNeeded, Inventory inventory) {
        int amount = amountNeeded;
        if (type instanceof GroupMaterialType) {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    if (((GroupMaterialType) type).getMaterials().contains(MaterialType.getMaterialType(is))) {
                        if(is.getAmount()>=amount) {
                            return true;
                        }else{
                            amount-=is.getAmount();
                        }
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
                                if(is.getAmount()>=amount) {
                                    return true;
                                }else{
                                    amount-=is.getAmount();
                                }
                            }
                }
            }
        } else {
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack is = inventory.getItem(i);
                if (is != null) {
                    if (type.getBase() == is.getType()) {
                        if(is.getAmount()>=amount) {
                            return true;
                        }else{
                            amount-=is.getAmount();
                        }
                    }
                }
            }
        }
        return false;
    }
}
