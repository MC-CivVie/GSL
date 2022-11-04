package me.zombie_striker.gsl.utils;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.recipes.FactoryRecipe;
import me.zombie_striker.gsl.wordbank.WordBank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemUtil {

    public static final ComponentBuilder LORE_WORDBANK = new ComponentBuilder("WB: ", ComponentBuilder.BLUE);

    public static ItemStack prepareFactoryIcon(FactoryRecipe factoryRecipe){
        ItemStack i = factoryRecipe.getIcon().toItemStack();
        ItemMeta im = i.getItemMeta();
        im.displayName(new ComponentBuilder(factoryRecipe.getDisplayname(),ComponentBuilder.WHITE).build());
        List<Component> ingredients = new LinkedList<>();
        for(Map.Entry<MaterialType, Integer> e : factoryRecipe.getIngredients().entrySet()){
            ingredients.add(new ComponentBuilder(e.getKey().getName()+":"+e.getValue(),ComponentBuilder.RED).build());
        }
        im.lore(ingredients);
        i.setItemMeta(im);
        return i;
    }

    public static Component getWordBankOfItemLore(ItemStack is){
        @Nullable List<Component> lores = is.lore();
        if (lores != null) {
            for (int loreindex = 0; loreindex < lores.size(); loreindex++) {
                Component lore = lores.get(loreindex);
                if (lore instanceof TextComponent) {
                    if (((TextComponent) lore).content().startsWith(((TextComponent) LORE_WORDBANK.build()).content())) {
                        return (lores.get(loreindex));
                    }
                }
            }
        }
        return null;
    }
    public static ItemStack addWordBankLore(String key, String displayname, ItemStack itemStack, Location bench) {
        @Nullable List<Component> lores = itemStack.lore();
        if (lores != null) {
            for (int loreindex = 0; loreindex < lores.size(); loreindex++) {
                Component lore = lores.get(loreindex);
                if (lore instanceof TextComponent) {
                    if (((TextComponent) lore).content().startsWith(((TextComponent) LORE_WORDBANK.build()).content())) {
                        lores.remove(loreindex);
                        break;
                    }
                }
            }
        }else{
            lores = new ArrayList<>();
        }

        int wordbank = 0;
        for(int letterindex = 0; letterindex < key.length(); letterindex++){
            wordbank+=(letterindex*key.charAt(letterindex));
        }

        int wordbank2 = bench.getBlockX()+bench.getBlockY()+bench.getBlockZ();


        lores.add(LORE_WORDBANK.clone().append(WordBank.getRandomWordbank(wordbank, wordbank2),ComponentBuilder.WHITE).build());

        ItemStack is = itemStack;
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(displayname));
        im.lore(lores);
        is.setItemMeta(im);
        return is;
    }
}
