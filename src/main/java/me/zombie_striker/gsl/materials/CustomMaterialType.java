package me.zombie_striker.gsl.materials;

import org.bukkit.Material;

public class CustomMaterialType extends MaterialType{

    private int id;

    public CustomMaterialType(String name, Material base, int id) {
        super(name, base);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
