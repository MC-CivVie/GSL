package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NamelayerCreateAd implements CommandExecutor, TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> r = new LinkedList<>();
            for (NameLayer nameLayer : NameLayer.getNameLayers()) {
                if (nameLayer.getMemberranks().containsKey(((Player) sender).getUniqueId())) {
                    r.add(nameLayer.getName());
                }
            }
            return r;
        } else if (args.length == 2) {
            return Arrays.asList("setDiscord", "setName", "setVisible", "setIdeology", "setIcon","setDescription");
        }
        if(args.length>2) {
            if (args[1].equalsIgnoreCase("setIcon")) {
                return Arrays.asList("CRAFTING_TABLE", "DIRT", "GOLD_BLOCK", "STONE");
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("You need to specify the name of the group you wish set the advertisement for.", ComponentBuilder.RED).build());
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
        if (!nameLayer1.getMemberranks().containsKey(player.getUniqueId()) || nameLayer1.getMemberranks().get(player.getUniqueId()) != 0) {
            sender.sendMessage(new ComponentBuilder("You are not the owner of " + nameLayer1.getName() + "!", ComponentBuilder.RED).build());
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(new ComponentBuilder("You need to specify the action.", ComponentBuilder.RED).build());
            return true;
        }

        String action = args[1];

        if (action.equalsIgnoreCase("setIcon")) {
            if (args.length >= 2) {
                Material material = Material.matchMaterial(args[2]);
                if(material==null ||material.isAir() || ! material.isItem()) {
                    sender.sendMessage(new ComponentBuilder("The input \""+args[2]+"\" is not a valid material type.", ComponentBuilder.RED).build());
                    return true;
                }
                nameLayer1.getCreateAdvertisement().setIcon(material);
                sender.sendMessage(new ComponentBuilder("Setting ad icon to " + material.name() + ".", ComponentBuilder.GREEN).build());
            }else{
                sender.sendMessage(new ComponentBuilder("Please specify your groups icon.", ComponentBuilder.RED).build());
            }
        } else if (action.equalsIgnoreCase("setIdeology")) {
            if (args.length >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) {
                        {
                            sb.append(" ");
                        }
                    }
                }
                nameLayer1.getCreateAdvertisement().setIdeology(sb.toString());
                sender.sendMessage(new ComponentBuilder("Setting ad ideology to " + sb.toString() + ".", ComponentBuilder.GREEN).build());
            } else {
                sender.sendMessage(new ComponentBuilder("Please specify your groups ideology.", ComponentBuilder.RED).build());
            }
        } else if (action.equalsIgnoreCase("setVisible")) {
            if (args.length > 2) {
                try {
                    boolean b = Boolean.parseBoolean(args[2]);
                    nameLayer1.setVisible(b);
                    nameLayer1.getCreateAdvertisement();
                    sender.sendMessage(new ComponentBuilder("Setting ad visibility to " + b + ".", ComponentBuilder.GREEN).build());
                } catch (Exception e) {
                    sender.sendMessage(new ComponentBuilder(args[2] + " is not true or false.", ComponentBuilder.RED).build());
                }
            } else {
                sender.sendMessage(new ComponentBuilder("Please specify whether you want the ad to be visible.", ComponentBuilder.RED).build());
            }

        } else if (action.equalsIgnoreCase("setName")) {

            if (args.length >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) {
                        {
                            sb.append(" ");
                        }
                    }
                }
                nameLayer1.getCreateAdvertisement().setDisplayname(sb.toString());
                sender.sendMessage(new ComponentBuilder("Setting ad name to " + sb.toString() + ".", ComponentBuilder.GREEN).build());
            } else {
                sender.sendMessage(new ComponentBuilder("Please specify your groups name.", ComponentBuilder.RED).build());
            }
        } else if (action.equalsIgnoreCase("setDescription")) {

            if (args.length >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) {
                        {
                            sb.append(" ");
                        }
                    }
                }
                nameLayer1.getCreateAdvertisement().setLore(sb.toString());
                sender.sendMessage(new ComponentBuilder("Setting ad description to " + sb.toString() + ".", ComponentBuilder.GREEN).build());
            } else {
                sender.sendMessage(new ComponentBuilder("Please specify your groups name.", ComponentBuilder.RED).build());
            }
        } else if (action.equalsIgnoreCase("setDiscord")) {
            if (args.length >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) {
                        {
                            sb.append(" ");
                        }
                    }
                }
                nameLayer1.getCreateAdvertisement().setDiscord(sb.toString());
                sender.sendMessage(new ComponentBuilder("Setting ad discord to " + sb.toString() + ".", ComponentBuilder.GREEN).build());
            } else {
                sender.sendMessage(new ComponentBuilder("Please specify your groups discord.", ComponentBuilder.RED).build());
            }
        } else {
            sender.sendMessage(new ComponentBuilder("Invalid option: " + action, ComponentBuilder.RED).build());
        }


        return false;
    }
}
