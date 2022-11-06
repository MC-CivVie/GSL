package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.handlers.NamelayerInviteHandler;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class NamelayerInviteCommand implements CommandExecutor, TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> r = new LinkedList<>();
        if (args.length == 1) {
            for (NameLayer nameLayer : NameLayer.getNameLayers()) {
                if (nameLayer.getMemberranks().containsKey(((Player) sender).getUniqueId())) {
                    r.add(nameLayer.getName());
                }
            }
            return r;
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("You need to specify the name of the group you wish to invite someone to.", ComponentBuilder.RED).build());
            return true;
        }
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        String nl1 = args[0];
        NameLayer nameLayer1 = NameLayer.getNameLayer(nl1);
        if (nameLayer1 == null) {
            sender.sendMessage(new ComponentBuilder("That namelayer does not exist!", ComponentBuilder.RED).build());
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(new ComponentBuilder("You need to specify the name of the player you wish to invite.", ComponentBuilder.RED).build());
            return true;
        }
        if (!nameLayer1.getMemberranks().containsKey(player.getUniqueId())) {
            sender.sendMessage(new ComponentBuilder("You are not in the group " + nameLayer1.getName() + "!", ComponentBuilder.RED).build());
            return true;
        }
        if (args.length == 2) {
            sender.sendMessage(new ComponentBuilder("You need to specify the rank of the player you wish to invite the player as.", ComponentBuilder.RED).build());
            return true;
        }
        OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
        int rank = 0;
        try {
            rank = Integer.parseInt(args[2]);
        } catch (Exception e43) {
            return true;
        }
        int playerRank = nameLayer1.getMemberranks().get(player.getUniqueId());
        if (playerRank > rank) {
            sender.sendMessage(new ComponentBuilder("You can only invite players as a rank lower than yourself (currently ", ComponentBuilder.RED).append(playerRank + "", ComponentBuilder.GRAY).append(").", ComponentBuilder.RED).build());
            return true;
        }
        NamelayerInviteHandler.invite(offplayer.getUniqueId(), nameLayer1, (byte) rank);
        sender.sendMessage(new ComponentBuilder("Inviting " + offplayer.getName() + " to " + nameLayer1.getName() + " with rank" + rank + ".", ComponentBuilder.GREEN).build());
        if(offplayer.isOnline())
            ((Player)offplayer).sendMessage(new ComponentBuilder(sender.getName()+" is inviting you to join " + nameLayer1.getName() + " with rank" + rank + ". Type ", ComponentBuilder.GREEN).append("/nlaccept "+nameLayer1.getName(),ComponentBuilder.GRAY).append(" to join this namelayer.",ComponentBuilder.GREEN).build());
        return false;
    }
}
