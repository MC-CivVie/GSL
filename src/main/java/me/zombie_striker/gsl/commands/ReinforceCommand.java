package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.reinforcement.ReinforcementMaterial;
import me.zombie_striker.gsl.states.PlayerStatesManager;
import me.zombie_striker.gsl.states.ReinforcementState;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ReinforceCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (PlayerStatesManager.getState(((Player) sender).getUniqueId(), ReinforcementState.class) != null) {
                    sender.sendMessage(new ComponentBuilder("No longer reinforcing.", ComponentBuilder.WHITE).build());
                    PlayerStatesManager.removeState(((Player) sender).getUniqueId(), ReinforcementState.class);

                } else {
                    sender.sendMessage(new ComponentBuilder("You need to specify as group to reinforce to.", ComponentBuilder.RED).build());
                }
                return true;
            }

            ReinforcementMaterial rm = null;
            if (player.getInventory().getItemInMainHand() != null)
                if ((rm = ReinforcementMaterial.getReinforcementMaterialData(MaterialType.getMaterialType(player.getInventory().getItemInMainHand()))) == null) {
                    sender.sendMessage(new ComponentBuilder("You need to hold the item you are going to reinforce with.", ComponentBuilder.RED).build());
                    return true;
                }


            String namelayer = args[0];
            NameLayer nl = NameLayer.getNameLayer(namelayer);
            if (nl != null) {
                if (nl.getMemberranks().containsKey(player.getUniqueId())) {
                    ReinforcementState rs = new ReinforcementState(nl, rm.getType());
                    PlayerStatesManager.addState(player.getUniqueId(), rs);
                    sender.sendMessage(new ComponentBuilder("Reinforcing to \"" + nl.getName() + "\".", ComponentBuilder.WHITE).build());
                }
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
