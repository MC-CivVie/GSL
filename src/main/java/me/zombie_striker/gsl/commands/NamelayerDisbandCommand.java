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

public class NamelayerDisbandCommand implements CommandExecutor, TabCompleter {
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
        if(args.length==0){
            sender.sendMessage(new ComponentBuilder("You need to specify the name of the group you wish to disband.",ComponentBuilder.RED).build());
            return true;
        }
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        String nl1 = args[0];
        NameLayer nameLayer1 = NameLayer.getNameLayer(nl1);
        if (nameLayer1 == null) {
            sender.sendMessage(new ComponentBuilder("That namelayer does not exist!", ComponentBuilder.RED).build());
            return true;
        }
        if(!nameLayer1.getMemberranks().containsKey(player.getUniqueId()) || nameLayer1.getMemberranks().get(player.getUniqueId())!=0){
            sender.sendMessage(new ComponentBuilder("You are not the owner of "+nameLayer1.getName()+"!",ComponentBuilder.RED).build());
            return true;
        }
        NameLayer.disband(nameLayer1);
        sender.sendMessage(new ComponentBuilder(nameLayer1.getName(),ComponentBuilder.GRAY).append(" has been disbanded",ComponentBuilder.GREEN).build());
        return false;
    }
}
