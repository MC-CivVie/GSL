package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class NamelayerMergeCommand implements CommandExecutor, TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> r = new LinkedList<>();
        for (NameLayer nameLayer : NameLayer.getNameLayers()) {
            if (nameLayer.getMemberranks().containsKey(((Player) sender).getUniqueId())) {
                r.add(nameLayer.getName());
            }
        }
        return r;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==0 || args.length==1){
            sender.sendMessage(new ComponentBuilder("You need to specify the name of the groups you wish to merge.",ComponentBuilder.RED).build());
            return true;
        }
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        String nl1 = args[0];
        String nl2 = args[1];
        NameLayer nameLayer1 = NameLayer.getNameLayer(nl1);
        NameLayer nameLayer2 = NameLayer.getNameLayer(nl2);
        if (nameLayer1 == null) {
            sender.sendMessage(new ComponentBuilder(nl1+" does not exist!", ComponentBuilder.RED).build());
            return true;
        }
        if (nameLayer2 == null) {
            sender.sendMessage(new ComponentBuilder(nl2+" does not exist!", ComponentBuilder.RED).build());
            return true;
        }
        if(!nameLayer1.getMemberranks().containsKey(player.getUniqueId()) || nameLayer1.getMemberranks().get(player.getUniqueId())!=0){
            sender.sendMessage(new ComponentBuilder("You are not the owner of "+nameLayer1.getName()+"!",ComponentBuilder.RED).build());
            return true;
        }
        if(!nameLayer2.getMemberranks().containsKey(player.getUniqueId()) || nameLayer2.getMemberranks().get(player.getUniqueId())!=0){
            sender.sendMessage(new ComponentBuilder("You are not the owner of "+nameLayer2.getName()+"!",ComponentBuilder.RED).build());
            return true;
        }
        nameLayer2.getMergers().add(nameLayer1.getUuid());
        NameLayer.disband(nameLayer1);
        sender.sendMessage(new ComponentBuilder(nameLayer1.getName(),ComponentBuilder.GRAY).append(" has been merged with ",ComponentBuilder.GREEN).append(nameLayer2.getName(),ComponentBuilder.GRAY).build());
        return false;
    }
}
