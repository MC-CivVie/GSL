package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.handlers.PrisonerHandler;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.ItemUtil;
import me.zombie_striker.gsl.utils.StringUtil;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class PrisonEvents implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getPlayer().getKiller() != null) {
            Component location = null;
            Player killer = event.getPlayer().getKiller();
            if (killer.getInventory().getItemInOffHand() != null && (location = ItemUtil.getPrisonFromLore(killer.getInventory().getItemInOffHand())) != null) {
                String locationstring = ((TextComponent) location).content();
                Location loc = StringUtil.getLocationFromString(locationstring);
                if (loc != null && loc.getBlock().getType() == Material.RESPAWN_ANCHOR) {
                    ItemStack is = killer.getInventory().getItemInOffHand();
                    if (is.getAmount() > 1) {
                        is.setAmount(is.getAmount() - 1);
                    } else {
                        is = null;
                    }
                    killer.getInventory().setItemInOffHand(is);
                    PrisonerHandler.imprison(event.getPlayer().getUniqueId(), loc);
                    return;
                }
            }
            for (int i = 0; i < 9; i++) {
                ItemStack is = killer.getInventory().getItem(i);
                if (is == null)
                    continue;
                if (is.getType() == Material.ENDER_PEARL && (location = ItemUtil.getPrisonFromLore(is)) != null) {
                    String locationstring = ((TextComponent) location).content();
                    Location loc = StringUtil.getLocationFromString(locationstring);
                    if (loc != null && loc.getBlock().getType() == Material.RESPAWN_ANCHOR) {
                        if (is.getAmount() > 1) {
                            is.setAmount(is.getAmount() - 1);
                        } else {
                            is = null;
                        }
                        killer.getInventory().setItemInOffHand(is);
                        PrisonerHandler.imprison(event.getPlayer().getUniqueId(), loc);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (PrisonerHandler.isPrisoner(event.getPlayer().getUniqueId())) {
            Location spawn = PrisonerHandler.getPrisonLocation(event.getPlayer().getUniqueId());
            spawn = spawn.add(0, 1, 0);
            event.setRespawnLocation(spawn);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.RESPAWN_ANCHOR) {
            for (Map.Entry<UUID, Location> e : new HashSet<>(PrisonerHandler.getPrisoners().entrySet())) {
                if (e.getValue().equals(event.getBlock().getLocation())) {
                    PrisonerHandler.free(e.getKey());
                    event.getPlayer().sendMessage(new ComponentBuilder(Bukkit.getOfflinePlayer(e.getKey()).getName(), ComponentBuilder.GRAY).append(" has been freed.", ComponentBuilder.GREEN).build());
                }
            }
        }
    }

    @EventHandler
    public void onBreakReinforce(BlockBreakEvent event) {
        if (PrisonerHandler.isPrisoner(event.getPlayer().getUniqueId())) {
            GSLChunk gslChunk = GSLChunk.getGSLChunk(event.getBlock().getChunk());
            GSLCube gslCube = gslChunk.getCubes()[(event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
            if (gslCube != null) {
                int x = event.getBlock().getX() % 16;
                if (event.getBlock().getX() < 0)
                    x = Math.abs((-event.getBlock().getX()) % 16 - 15);
                int z = event.getBlock().getZ() % 16;
                if (event.getBlock().getZ() < 0)
                    z = Math.abs((-event.getBlock().getZ()) % 16 - 15);

                int y = (event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) % 16;
                if (gslCube.getDurability()[x][y][z] > 0) {
                    if (!gslCube.getNamelayers()[x][y][z].getMemberranks().containsKey(event.getPlayer().getUniqueId())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(new ComponentBuilder("You cannot break reinforced blocks while being imprisoned", ComponentBuilder.RED).build());

                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            return;
        if (event.getClickedBlock() == null)
            return;
        if (event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInMainHand().getType() != Material.ENDER_PEARL)
            return;
        if (event.getClickedBlock().getType() != Material.RESPAWN_ANCHOR)
            return;
        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();
        hand = ItemUtil.addPrisonToLore(hand, event.getClickedBlock().getLocation());
        event.getPlayer().getInventory().setItemInMainHand(hand);
        event.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
    }
}
