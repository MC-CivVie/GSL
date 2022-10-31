package me.zombie_striker.gsl.items;

import me.zombie_striker.gsl.materials.MaterialType;

public class ItemHolder {

    private MaterialType materialType;
    private int amount;

    public ItemHolder(MaterialType materialType, int amount){
        this.materialType = materialType;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }
}
