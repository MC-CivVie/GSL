package me.zombie_striker.gsl.materials;

import org.bukkit.Material;

import java.util.List;

public class GroupMaterialType extends MaterialType{

    private List<MaterialType> materials;

    public GroupMaterialType(String name, List<MaterialType> materials) {
        super(name, null);
        this.materials = materials;
    }

    public List<MaterialType> getMaterials() {
        return materials;
    }
}
