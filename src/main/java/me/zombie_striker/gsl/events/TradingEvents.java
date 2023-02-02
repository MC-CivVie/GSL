package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.GSL;
import me.zombie_striker.gsl.data.Pair;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.utils.InventoryUtil;
import me.zombie_striker.gsl.utils.ItemUtil;
import me.zombie_striker.gsl.utils.guis.GUI;
import me.zombie_striker.gsl.utils.guis.GUIAction;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TradingEvents implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.STONE_BUTTON && event.getItem().getItemMeta().hasDisplayName()) {
            event.setCancelled(true);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.LECTERN) {
                GSLChunk gslChunk = GSLChunk.getGSLChunk(event.getClickedBlock().getChunk());
                GSLCube gslCube = gslChunk.getCubes()[(event.getClickedBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
                if (gslCube == null) {
                    gslCube = new GSLCube();
                    gslChunk.getCubes()[(event.getClickedBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16] = gslCube;
                }

                int x = event.getClickedBlock().getX() % 16;
                if (event.getClickedBlock().getX() < 0)
                    x = Math.abs((-event.getClickedBlock().getX()) % 16 - 15);
                int z = event.getClickedBlock().getZ() % 16;
                if (event.getClickedBlock().getZ() < 0)
                    z = Math.abs((-event.getClickedBlock().getZ()) % 16 - 15);
                int y = (event.getClickedBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) % 16;

                if (gslCube.getNamelayers()[x][y][z] != null) {
                    openTempGUI(event.getPlayer(), event.getClickedBlock(), gslCube.getNamelayers()[x][y][z]);
                }
            }
        }
    }

    private void openTempGUI(Player player, Block clickedBlock, NameLayer shopnamelayer) {
        GUI guitemp = new GUI(Bukkit.createInventory(null, 9, "Loading Shop GUI..."));
        GUI shopGUI = new GUI(Bukkit.createInventory(null, 54, "Shop GUI For > " + shopnamelayer.getName()));
        new BukkitRunnable() {
            List<Location> toCheck = new ArrayList<>(Arrays.asList(clickedBlock.getLocation()));
            List<Location> checked = new ArrayList<>();
            int iteration = 0;
            int shopindex = 0;

            public void run() {
                for (Location location : toCheck) {
                    Inventory testcheckl = getChestInventoryFromSign(location.getBlock(),shopnamelayer);
                    if (testcheckl != null) {
                        for (ItemStack is : testcheckl.getContents()) {
                            if (is == null)
                                continue;
                            if (is.getType() == Material.STONE_BUTTON && is.getItemMeta().getDisplayName().equalsIgnoreCase("Trade Button")) {

                                String[] tradeinput = ItemUtil.getTradeButtonInput(is).split(":");
                                String[] tradeoutput = ItemUtil.getTradeButtonOutput(is).split(":");


                                Pair<MaterialType, Integer> trade_price = new Pair<>(MaterialType.getMaterialType(tradeinput[0]), Integer.parseInt(tradeinput[1]));
                                Pair<MaterialType, Integer> trade_product = new Pair<>(MaterialType.getMaterialType(tradeoutput[0]), Integer.parseInt(tradeoutput[1]));
                                int maxTrades = getAmountOfTrades(testcheckl, trade_product.getFirst(), trade_product.getSecond());
                                if (maxTrades > 0) {
                                    ItemStack tradeoffer = ItemUtil.prepareTradeItemIcon(trade_product, trade_price, maxTrades);
                                    shopGUI.setIcon(shopindex, tradeoffer, new GUIAction() {
                                        @Override
                                        public void click(int slot, Player player, GUI gui) {
                                            InventoryUtil.trade(testcheckl, player.getInventory(), trade_product, trade_price, player.getLocation());
                                            int maxTrades = getAmountOfTrades(testcheckl, trade_product.getFirst(), trade_product.getSecond());
                                            ItemStack tradeoffer = ItemUtil.prepareTradeItemIcon(trade_product, trade_price, maxTrades);
                                            shopGUI.setDisplayIcon(slot, tradeoffer);
                                            if (maxTrades == 0) {
                                                player.closeInventory();
                                                openTempGUI(player, clickedBlock, shopnamelayer);
                                            }

                                        }
                                    });
                                    shopindex++;
                                    if (shopindex == 54) {
                                        player.openInventory(shopGUI.getInventory());
                                        cancel();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                List<Location> temptocheck = new ArrayList<>(toCheck);
                toCheck.clear();
                for (Location location : temptocheck) {
                    checked.add(location);
                    Location temp = location.clone().add(0, 0, 1);
                    if (!checked.contains(temp))
                        toCheck.add(temp);
                    temp = location.clone().add(0, 0, -1);
                    if (!checked.contains(temp))
                        toCheck.add(temp);
                    temp = location.clone().add(0, 1, 0);
                    if (!checked.contains(temp))
                        toCheck.add(temp);
                    temp = location.clone().add(0, -1, 0);
                    if (!checked.contains(temp))
                        toCheck.add(temp);
                    temp = location.clone().add(1, 0, 0);
                    if (!checked.contains(temp))
                        toCheck.add(temp);
                    temp = location.clone().add(-1, 0, 0);
                    if (!checked.contains(temp))
                        toCheck.add(temp);
                    iteration++;
                    if (iteration == 64) {
                        player.openInventory(shopGUI.getInventory());
                        cancel();
                        return;
                    }
                    guitemp.setDisplayIcon(4, ItemUtil.setAmount(ItemUtil.prepareItem(Material.GLASS_PANE, "Loading Nearby Shops..."), iteration));
                }

            }
        }.runTaskTimer(GSL.getCore(), 1, 1);
        player.openInventory(guitemp.getInventory());
    }

    private int getAmountOfTrades(Inventory testcheckl, MaterialType mt, Integer second) {
        int count = 0;
        for (ItemStack is : testcheckl.getContents()) {
            if (is != null && MaterialType.getMaterialType(is) == mt)
                count += is.getAmount();
        }
        return count / second;
    }

    public Inventory getChestInventoryFromSign(Block chestrel, NameLayer nl) {
        if (chestrel.getType() == Material.CHEST) {
            GSLChunk gslChunk2 = GSLChunk.getGSLChunk(chestrel.getChunk());
            GSLCube gslCube2 = gslChunk2.getCubes()[(chestrel.getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
            if (gslCube2 == null) {
                gslCube2 = new GSLCube();
                gslChunk2.getCubes()[(chestrel.getY() - GSLChunk.BLOCK_Y_OFFSET) / 16] = gslCube2;
            }

            int x2 = chestrel.getX() % 16;
            if (chestrel.getX() < 0)
                x2 = Math.abs((-chestrel.getX()) % 16 - 15);
            int z2 = chestrel.getZ() % 16;
            if (chestrel.getZ() < 0)
                z2 = Math.abs((-chestrel.getZ()) % 16 - 15);
            int y2 = (chestrel.getY() - GSLChunk.BLOCK_Y_OFFSET) % 16;

            if (gslCube2.getNamelayers()[x2][y2][z2]==nl) {
                //Same Namelayer
                Container container = (Container) chestrel.getState();
                Inventory inv = container.getInventory();
                return inv;
            }
        }
        return null;
    }
}
