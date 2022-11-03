package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.utils.ItemUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class WordBankEvents implements Listener {
    
    @EventHandler
    public void onAnvil(PrepareAnvilEvent event){
        if(event.getInventory().getFirstItem()!=null) {
            if (event.getInventory().getFirstItem().getType() == Material.NAME_TAG) {
                event.getInventory().setRepairCost(0);
                ItemStack result = event.getResult();
                if(result!=null)
                result = ItemUtil.addWordBankLore(event.getInventory().getRenameText(),"WordBank Key", result, event.getInventory().getLocation());
                event.setResult(result);
            }
            if(event.getInventory().getSecondItem()!=null && event.getInventory().getSecondItem().getType()==Material.NAME_TAG){
                if(event.getInventory().getSecondItem().hasItemMeta()&&event.getInventory().getSecondItem().getItemMeta().hasDisplayName()){
                    event.getInventory().setRepairCost(0);
                    ItemStack result = event.getInventory().getFirstItem().clone();
                    if(ItemUtil.getWordBankOfItemLore(result)==null) {
                        ItemMeta im = result.getItemMeta();
                        List<Component> lore = im.lore();
                        if (lore == null)
                            lore = new LinkedList<>();
                        lore.add(ItemUtil.getWordBankOfItemLore(event.getInventory().getSecondItem()));
                        im.lore(lore);
                        result.setItemMeta(im);
                    }
                    event.setResult(result);
                }
            }
        }
    }
}
