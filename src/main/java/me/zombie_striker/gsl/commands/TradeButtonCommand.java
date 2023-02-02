package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class TradeButtonCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length<4){
            commandSender.sendMessage(new ComponentBuilder("Command structure:",ComponentBuilder.RED).append("/TB <Input Material> <Input Amount> <Output Material> <Output Amount>",ComponentBuilder.WHITE).build());
            return true;
        }
        MaterialType in1 = MaterialType.getMaterialType(args[0]);
        int in2 = Integer.parseInt(args[1]);
        MaterialType out1 = MaterialType.getMaterialType(args[2]);
        int out2 = Integer.parseInt(args[3]);
        ItemStack shopbutton = ItemUtil.prepareTradeButton(in1,in2,out1,out2);
        if(commandSender instanceof Player){
            ((Player) commandSender).getInventory().addItem(shopbutton);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tab = new LinkedList<>();
        if(args.length == 1 || args.length==3){
            for(Material material : Material.values()){
                if(material.name().startsWith(args[args.length-1].toUpperCase())){
                    tab.add(material.name());
                }
            }
        }
        if(args.length==2 || args.length==4){
            tab.add("1");
            tab.add("2");
            tab.add("3");
        }
        return tab;
    }
}
