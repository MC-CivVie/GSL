package me.zombie_striker.gsl.materials;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    @Override
    public ItemStack toItemStack() {
        for(MaterialType ing : materials){
            ItemStack is = ing.toItemStack();
            if(is !=null)
            return is;
        }
        return null;
    }
}
