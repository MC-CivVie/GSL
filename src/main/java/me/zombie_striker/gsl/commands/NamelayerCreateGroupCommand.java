package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NamelayerCreateGroupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==0){
            sender.sendMessage(new ComponentBuilder("You need to provide a name for the group you wish to create.",ComponentBuilder.RED).build());
            return true;
        }
        if(args.length > 1){
            sender.sendMessage(new ComponentBuilder("Names cannot contain spaces.",ComponentBuilder.RED).build());
            return true;
        }
        String arg = args[0];
        if(NameLayer.getNameLayer(arg)!=null){
            sender.sendMessage(new ComponentBuilder("That name has been taken.",ComponentBuilder.RED).build());
            return true;
        }
        NameLayer nl = new NameLayer(arg, UUID.randomUUID());
        nl.addMemberRank(((Player)sender).getUniqueId(),(byte)0);
        NameLayer.register(nl);
        sender.sendMessage(new ComponentBuilder("Namelayer "+nl.getName()+" has been successfully created.",ComponentBuilder.GREEN).build());
        return false;
    }
}
