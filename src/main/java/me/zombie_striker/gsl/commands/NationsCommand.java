package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.ItemUtil;
import me.zombie_striker.gsl.utils.MapUtil;
import me.zombie_striker.gsl.utils.guis.GUI;
import me.zombie_striker.gsl.utils.guis.GUIAction;
import me.zombie_striker.gsl.utils.guis.GUIUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Map<Object, Object> sorted = i.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(o -> o.getKey(), o -> o.getValue(), (e1, e2) -> e1, LinkedHashMap::new));

        final int[] slot = {0};
        sorted.forEach((e, o)  -> {
            ItemStack is = ItemUtil.prepareAd(((Map.Entry<NameLayer,Integer>)e).getKey().getAdvertisement(), ((Map.Entry<NameLayer,Integer>)e).getValue(),((Map.Entry<NameLayer,Integer>)e).getKey().getMemberranks().size());
            gui.setIcon(slot[0], is, new GUIAction() {
                @Override
                public void click(int slot, Player player, GUI gui) {
                    player.closeInventory();
                    player.sendMessage(new ComponentBuilder(((Map.Entry<NameLayer,Integer>)e).getKey().getName()+": ",ComponentBuilder.WHITE).appendClickableURL(((Map.Entry<NameLayer,Integer>)e).getKey().getAdvertisement().getDiscord(),ComponentBuilder.LIGHT_BLUE).build());
                }
            });
            slot[0]++;
        });
        player.openInventory(gui.getInventory());

        GUIUtil.register(gui);
        return false;
    }
}
