package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.GSL;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.ItemUtil;
import me.zombie_striker.gsl.utils.guis.GUI;
import me.zombie_striker.gsl.utils.guis.GUIAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class NamelayerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        openGUI((Player)sender);
        return true;
    }

    private void openGUI(Player sender) {
        GUI gui = new GUI(Bukkit.createInventory(null,54,"NameLayer"));
        int i = 0;
        for(NameLayer nl : NameLayer.getNameLayers()){
            if(nl.getMemberranks().containsKey(sender.getUniqueId())){
                gui.setIcon(i, ItemUtil.prepareItem(Material.IRON_CHESTPLATE, nl.getName()), new GUIAction() {
                    @Override
                    public void click(int slot, Player player, GUI gui) {
                        player.closeInventory();
                        openNamelayerGUI(nl,sender);
                    }
                });
                i++;
                if(i>=54)
                    break;
            }
        }
        sender.openInventory(gui.getInventory());
    }
    public void openNamelayerGUI(NameLayer nameLayer, Player player){
        GUI gui = new GUI(Bukkit.createInventory(null,54,"NameLayer>"+nameLayer.getName()));
        int playerindex = 9;
        for(Map.Entry<UUID, Byte> e : nameLayer.getMemberranks().entrySet()){
            gui.setIcon(playerindex,ItemUtil.prepareItem(Material.IRON_CHESTPLATE,Bukkit.getOfflinePlayer(e.getKey()).getName()), new GUIAction() {
                @Override
                public void click(int slot, Player player, GUI gui) {
                    player.closeInventory();
                    openPlayerEditor(nameLayer,e.getKey(),e.getValue(),player);
                }
            });
            playerindex++;
            if(playerindex>=54)
                break;
        }
        player.openInventory(gui.getInventory());
    }
    public void openPlayerEditor(NameLayer nameLayer, UUID who, int rank, Player sender){
        OfflinePlayer whop = Bukkit.getOfflinePlayer(who);
        GUI gui = new GUI(Bukkit.createInventory(null,9,"NameLayer>"+nameLayer.getName()+">"+whop.getName()));

        if(nameLayer.getMemberranks().get(sender.getUniqueId()) < rank) {
            gui.setIcon(0, ItemUtil.prepareItem(Material.BARRIER, "Kick from group"), new GUIAction() {
                @Override
                public void click(int slot, Player player, GUI gui) {
                    nameLayer.getMemberranks().remove(who);
                    sender.sendMessage(new ComponentBuilder("[NL]",ComponentBuilder.GREEN).append("Kicked "+whop.getName()+" from the namelayer.",ComponentBuilder.WHITE).build());
                    sender.closeInventory();
                    openNamelayerGUI(nameLayer,sender);
                }
            });
        }
        gui.setIcon(4, ItemUtil.setAmount(ItemUtil.prepareItem(Material.IRON_CHESTPLATE, "Rank of "+whop.getName()),Math.max(1,Math.min(64,rank))), new GUIAction() {
            @Override
            public void click(int slot, Player player, GUI gui) {
            }
        });

        sender.openInventory(gui.getInventory());
    }
}
