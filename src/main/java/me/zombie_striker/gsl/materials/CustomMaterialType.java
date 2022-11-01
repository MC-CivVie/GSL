package me.zombie_striker.gsl.materials;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomMaterialType extends MaterialType{

    private int id;
    private String displayname;

    public CustomMaterialType(String name, String displayname, Material base, int id) {
        super(name, base);
        this.id = id;
        this.displayname=  displayname;
    }

    public int getId() {
        return id;
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack is = super.toItemStack();
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(id);
        im.displayName(Component.text(displayname));
        is.setItemMeta(im);
        return is;
    }
}
