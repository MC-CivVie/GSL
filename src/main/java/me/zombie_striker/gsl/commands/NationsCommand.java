package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.ItemUtil;
import me.zombie_striker.gsl.utils.MapUtil;
import me.zombie_striker.gsl.utils.guis.GUI;
import me.zombie_striker.gsl.utils.guis.GUIAction;
import me.zombie_striker.gsl.utils.guis.GUIUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NationsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;
        GUI gui = new GUI(Bukkit.createInventory(null,54,"Nations"));

        HashMap<NameLayer, Integer>  activePlayers = new HashMap<>();

        for(NameLayer nl : NameLayer.getNameLayers()){
            if(nl.isVisible()){
                int count = 0;
                for(UUID uuid : nl.getMemberranks().keySet()){
                    if(Bukkit.getPlayer(uuid)!=null)
                        count++;
                }
                activePlayers.put(nl,count);
            }
        }

        Map<NameLayer,Integer> i = MapUtil.sortByValue(activePlayers);
        int slot = 0;
        for(Map.Entry<NameLayer, Integer> e : i.entrySet()){
            ItemStack is = ItemUtil.prepareAd(e.getKey().getAdvertisement(), e.getValue(),e.getKey().getMemberranks().size());
            gui.setIcon(slot, is, new GUIAction() {
                @Override
                public void click(int slot, Player player, GUI gui) {
                    player.closeInventory();
                    player.sendMessage(new ComponentBuilder(e.getKey().getName()+": ",ComponentBuilder.WHITE).appendClickableURL(e.getKey().getAdvertisement().getDiscord(),ComponentBuilder.LIGHT_BLUE).build());
                }
            });
        }
        player.openInventory(gui.getInventory());

        GUIUtil.register(gui);
        return false;
    }
}
