package me.zombie_striker.gsl.utils;

import me.zombie_striker.gsl.data.Pair;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NLAdvertisement;
import me.zombie_striker.gsl.recipes.FactoryRecipe;
import me.zombie_striker.gsl.wordbank.WordBank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemUtil {

    public static final ComponentBuilder LORE_WORDBANK = new ComponentBuilder("WB: ", ComponentBuilder.BLUE);
    public static final ComponentBuilder LORE_PRISON = new ComponentBuilder("Prison: ", ComponentBuilder.BLUE);

    public static ItemStack prepareAd(NLAdvertisement advertisement, int playercount, int playertotal){
        ItemStack is = new ItemStack(advertisement.getIcon(),Math.min(64,playercount));
        ItemMeta im = is.getItemMeta();
        im.displayName(new ComponentBuilder(advertisement.getDisplayname(),ComponentBuilder.LIGHT_BLUE).build());
        List<Component> lore = new LinkedList<>();

        lore.add(new ComponentBuilder("Ad: ",ComponentBuilder.LIGHT_BLUE).append(advertisement.getLore(),ComponentBuilder.WHITE).build());
        lore.add(new ComponentBuilder("Ideology: ",ComponentBuilder.LIGHT_BLUE).append(advertisement.getIdeology(),ComponentBuilder.WHITE).build());
        lore.add(new ComponentBuilder("Members: ",ComponentBuilder.LIGHT_BLUE).append(playercount+"/"+playertotal+" Online",ComponentBuilder.WHITE).build());
        lore.add(new ComponentBuilder("Discord: ",ComponentBuilder.LIGHT_BLUE).append(advertisement.getDiscord(),ComponentBuilder.WHITE).build());
        im.lore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack prepareFactoryIcon(FactoryRecipe factoryRecipe){
        ItemStack i = factoryRecipe.getIcon().toItemStack();
        ItemMeta im = i.getItemMeta();
        im.displayName(new ComponentBuilder(factoryRecipe.getDisplayname(),ComponentBuilder.WHITE).build());
        List<Component> ingredients = new LinkedList<>();
        for(Map.Entry<MaterialType, Integer> e : factoryRecipe.getIngredients().entrySet()){
            if(e.getKey()!=null)
            ingredients.add(new ComponentBuilder(e.getKey().getName()+":"+e.getValue(),ComponentBuilder.RED).build());
        }
        ingredients.add(new ComponentBuilder("--------------------",ComponentBuilder.GRAY).build());
        for(Map.Entry<MaterialType, Integer> e : factoryRecipe.getResults().entrySet()){
            if(e.getKey()!=null)
            ingredients.add(new ComponentBuilder(e.getKey().getName()+":"+e.getValue(),ComponentBuilder.GREEN).build());
        }
        im.lore(ingredients);
        i.setItemMeta(im);
        return i;
    }

    public static Component getPrisonFromLore(ItemStack is){
        @Nullable List<Component> lores = is.lore();
        if (lores != null) {
            for (int loreindex = 0; loreindex < lores.size(); loreindex++) {
                Component lore = lores.get(loreindex);
                if (lore instanceof TextComponent) {
                    if (((TextComponent) lore).content().startsWith(((TextComponent) LORE_PRISON.build()).content())) {
                        return (lores.get(loreindex));
                    }
                }
            }
        }
        return null;
    }
    public static ItemStack addPrisonToLore(ItemStack itemStack, Location prison) {
        @Nullable List<Component> lores = itemStack.lore();
        if (lores != null) {
            for (int loreindex = 0; loreindex < lores.size(); loreindex++) {
                Component lore = lores.get(loreindex);
                if (lore instanceof TextComponent) {
                    if (((TextComponent) lore).content().startsWith(((TextComponent) LORE_PRISON.build()).content())) {
                        lores.remove(loreindex);
                        break;
                    }
                }
            }
        }else{
            lores = new ArrayList<>();
        }

        lores.add(LORE_PRISON.clone().append(StringUtil.getStringLocation(prison),ComponentBuilder.WHITE).build());

        ItemStack is = itemStack;
        ItemMeta im = is.getItemMeta();
        im.lore(lores);
        is.setItemMeta(im);
        return is;
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

    public static ItemStack setAmount(ItemStack toItemStack, Integer value) {
        toItemStack.setAmount(value);
        return toItemStack;
    }

    public static ItemStack prepareItem(Material material, String displayname, Component... lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta im =is.getItemMeta();
        im.displayName(Component.text(displayname));

        List<Component> l = new LinkedList<>();
        for(Component e : lore){
            l.add(e);
        }
        im.lore(l);
        is.setItemMeta(im);
        return is;
    }

    public static String getTradeButtonInput(ItemStack is){
        @Nullable List<Component> lores = is.lore();
        if (lores != null) {
            for (int loreindex = 0; loreindex < lores.size(); loreindex++) {
                Component lore = lores.get(loreindex);
                if (lore instanceof TextComponent) {
                    if (((TextComponent) lore).content().startsWith("Input:")) {
                        return ((TextComponent)lores.get(loreindex)).content().split(":",2)[1];
                    }
                }
            }
        }
        return null;
    }
    public static String getTradeButtonOutput(ItemStack is){
        @Nullable List<Component> lores = is.lore();
        if (lores != null) {
            for (int loreindex = 0; loreindex < lores.size(); loreindex++) {
                Component lore = lores.get(loreindex);
                if (lore instanceof TextComponent) {
                    if (((TextComponent) lore).content().startsWith("Output:")) {
                        return ((TextComponent)lores.get(loreindex)).content().split(":",2)[1];
                    }
                }
            }
        }
        return null;
    }
    public static ItemStack prepareTradeButton(MaterialType tradeitem, int tradeamount, MaterialType costitem, int costamount){
        ItemStack is = prepareItem(Material.STONE_BUTTON,"Trade Button",
                new ComponentBuilder("Input: "+tradeitem.getName()+":"+tradeamount,ComponentBuilder.WHITE).build(),
                new ComponentBuilder("Output: "+ costitem.getName()+":"+costamount,ComponentBuilder.WHITE).build()
                );
        return is;
    }

    public static ItemStack prepareTradeItemIcon(Pair<MaterialType, Integer> trade, Pair<MaterialType, Integer> shop, int maxTrades) {
        if(maxTrades<=0){
            return null;
        }
        return setAmount(prepareItem(trade.getFirst().toItemStack().getType(),trade.getFirst().getName()+" x "+trade.getSecond(),
                new ComponentBuilder("Price: "+shop.getFirst().getName()+" x "+shop.getSecond(),ComponentBuilder.LIGHT_BLUE).build()
                , new ComponentBuilder("Trades left: "+maxTrades,ComponentBuilder.GOLD).build()),trade.getSecond());
    }
}
