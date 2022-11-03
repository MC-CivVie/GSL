package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.reinforcement.ReinforcementMaterial;
import me.zombie_striker.gsl.states.PlayerStatesManager;
import me.zombie_striker.gsl.states.ReinforcementState;
import me.zombie_striker.gsl.states.TalkInGroupState;
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

public class GroupCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (PlayerStatesManager.getState(((Player) sender).getUniqueId(), TalkInGroupState.class) != null) {
                    sender.sendMessage(new ComponentBuilder("Talking in [!].", ComponentBuilder.WHITE).build());
                    PlayerStatesManager.removeState(((Player) sender).getUniqueId(), TalkInGroupState.class);

                } else {
                    sender.sendMessage(new ComponentBuilder("You need to specify as group to chat in.", ComponentBuilder.RED).build());
                }
                return true;
            }


            String namelayer = args[0];
            NameLayer nl = NameLayer.getNameLayer(namelayer);
            if (nl != null) {
                if (nl.getMemberranks().containsKey(player.getUniqueId())) {
                    TalkInGroupState tigs = new TalkInGroupState(nl);
                    PlayerStatesManager.addState(player.getUniqueId(), tigs);
                    sender.sendMessage(new ComponentBuilder("Talking in [" + nl.getName() + "].", ComponentBuilder.WHITE).build());
                }
            }else{
                sender.sendMessage(new ComponentBuilder("Thew namelayer "+namelayer+" does not exist!", ComponentBuilder.RED).build());
                return true;
            }
        }
        return false;
    }

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
}
