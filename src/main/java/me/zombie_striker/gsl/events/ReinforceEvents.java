package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.handlers.BossBarHandler;
import me.zombie_striker.gsl.handlers.PrisonerHandler;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.reinforcement.ReinforcementMaterial;
import me.zombie_striker.gsl.states.PlayerState;
import me.zombie_striker.gsl.states.PlayerStatesManager;
import me.zombie_striker.gsl.states.ReinforcementState;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.InventoryUtil;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import org.bukkit.block.Block;
import org.bukkit.block.DoubleChest;
import org.bukkit.boss.BarColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public class ReinforceEvents implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        PlayerState ps = PlayerStatesManager.getState(event.getPlayer().getUniqueId(), ReinforcementState.class);
        if (ps != null) {
            NameLayer nl = ((ReinforcementState) ps).getNameLayer();
            MaterialType reinforcematerial = ((ReinforcementState) ps).getReinforceGroup();

            GSLChunk gslChunk = GSLChunk.getGSLChunk(event.getBlock().getChunk());
            GSLCube gslCube = gslChunk.getCubes()[(event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
            if (gslCube == null) {
                gslCube = new GSLCube();
                gslChunk.getCubes()[(event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16] = gslCube;
            }
            int x = event.getBlock().getX() % 16;
            if (event.getBlock().getX() < 0)
                x = Math.abs((-event.getBlock().getX()) % 16 - 15);
            int z = event.getBlock().getZ() % 16;
            if (event.getBlock().getZ() < 0)
                z = Math.abs((-event.getBlock().getZ()) % 16 - 15);

            int y = event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET;

            ReinforcementMaterial rm = ReinforcementMaterial.getReinforcementMaterialData(reinforcematerial);

            gslCube.getNamelayers()[x][y][z] = nl;
            gslCube.getPlaced()[x][y][z] = true;
            gslCube.getDurability()[x][y][z] = rm.getDurability();
            gslCube.getReinforcedBy()[x][y][z] = rm.getType();
            InventoryUtil.removeAmount(rm.getType(), 1, event.getPlayer().getInventory());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
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
            if (gslCube.getDurability()[x][y][z] >= 0) {
                if (gslCube.getNamelayers()[x][y][z] != null && !gslCube.getNamelayers()[x][y][z].getMemberranks().containsKey(event.getPlayer().getUniqueId())) {
                    if (PrisonerHandler.isPrisoner(event.getPlayer().getUniqueId())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(new ComponentBuilder("You cannot break reinforced blocks as a prisoner", ComponentBuilder.RED).build());
                        return;
                    }
                    gslCube.getDurability()[x][y][z] = gslCube.getDurability()[x][y][z] - 1;
                    if (gslCube.getDurability()[x][y][z] > 0) {

                        int maxdurability = ReinforcementMaterial.getReinforcementMaterialData(gslCube.getReinforcedBy()[x][y][z]).getDurability();
                        double d = (1.0 * gslCube.getDurability()[x][y][z]) / (maxdurability);

                        BossBarHandler.setBossbarsStats(event.getPlayer(), event.getBlock().getType().name() + ": " + gslCube.getDurability()[x][y][z] + "/" + maxdurability, d, BarColor.RED);
                        event.setCancelled(true);
                        return;
                    }else{
                        BossBarHandler.setBossbarsStats(event.getPlayer(), event.getBlock().getType().name() + ": " + "0/0", 0, BarColor.WHITE);
                        gslCube.getNamelayers()[x][y][z] = null;
                        gslCube.getReinforcedBy()[x][y][z] = null;
                        gslCube.getDurability()[x][y][z] = -1;
                    }
                } else {
                    MaterialType mt = gslCube.getReinforcedBy()[x][y][z];
                    if(mt!=null) {
                        ItemStack is = mt.toItemStack();
                        int first = event.getPlayer().getInventory().firstEmpty();
                        if (first != -1) {
                            event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), is);
                        } else {
                            event.getPlayer().getInventory().addItem(is);
                        }
                    }
                }
                gslCube.getNamelayers()[x][y][z] = null;
                gslCube.getReinforcedBy()[x][y][z] = null;
                gslCube.getDurability()[x][y][z] = -1;
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            return;
        if (event.getClickedBlock() == null)
            return;
        PlayerState ps = PlayerStatesManager.getState(event.getPlayer().getUniqueId(), ReinforcementState.class);
        if (ps != null) {
            NameLayer nl = ((ReinforcementState) ps).getNameLayer();
            MaterialType reinforcematerial = ((ReinforcementState) ps).getReinforceGroup();
            ReinforcementMaterial rm = ReinforcementMaterial.getReinforcementMaterialData(reinforcematerial);


            ItemStack inhand = event.getPlayer().getInventory().getItemInMainHand();
            MaterialType handtype = MaterialType.getMaterialType(inhand);
            if(!reinforcematerial.equals(handtype))
                return;

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

            if (gslCube.getNamelayers()[x][y][z] == null) {
                gslCube.getNamelayers()[x][y][z] = nl;
                gslCube.getReinforcedBy()[x][y][z] = reinforcematerial;
                gslCube.getDurability()[x][y][z] = rm.getDurability();
                if (inhand.getAmount() == 1) {
                    inhand = null;
                } else {
                    inhand.setAmount(inhand.getAmount() - 1);
                }
                event.getPlayer().getInventory().setItemInMainHand(inhand);
            } else {
                if (gslCube.getNamelayers()[x][y][z].getMemberranks().containsKey(event.getPlayer().getUniqueId())) {
                    if (gslCube.getReinforcedBy()[x][y][z] != null && gslCube.getReinforcedBy()[x][y][z].getBase()!=reinforcematerial.getBase())
                        event.getPlayer().getInventory().addItem(gslCube.getReinforcedBy()[x][y][z].toItemStack());

                    gslCube.getNamelayers()[x][y][z] = nl;
                    gslCube.getReinforcedBy()[x][y][z] = reinforcematerial;
                    gslCube.getDurability()[x][y][z] = rm.getDurability();
                    if (inhand.getAmount() == 1) {
                        inhand = null;
                    } else {
                        inhand.setAmount(inhand.getAmount() - 1);
                    }
                    event.getPlayer().getInventory().setItemInMainHand(inhand);
                } else {
                    event.getPlayer().sendMessage(new ComponentBuilder("You cannot reinforce this block, as it is already reinforced to a foreign group.", ComponentBuilder.RED).build());
                }
            }
        }
    }

    @EventHandler
    public void onInteractiveBlock(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            return;
        if (event.getClickedBlock() == null)
            return;
        if(event.getAction().isLeftClick())
            return;
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
        if (gslCube.getNamelayers()[x][y][z] != null && !gslCube.getNamelayers()[x][y][z].getMemberranks().containsKey(event.getPlayer().getUniqueId())) {
            if (event.getClickedBlock().getType().isInteractable()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(new ComponentBuilder("This ", ComponentBuilder.RED).append(event.getClickedBlock().getType().name(), ComponentBuilder.GRAY).append(" is locked.", ComponentBuilder.RED).build());
            }
        }
        if(event.getClickedBlock().getState() instanceof DoubleChest){
            Block other = (Block) ((DoubleChest) event.getClickedBlock().getState()).getLeftSide();
            if(other.getLocation().equals(event.getClickedBlock().getLocation()))
                other = (Block) ((DoubleChest) event.getClickedBlock().getState()).getRightSide();

            gslChunk = GSLChunk.getGSLChunk(event.getClickedBlock().getChunk());
            gslCube = gslChunk.getCubes()[(event.getClickedBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
            if (gslCube == null) {
                gslCube = new GSLCube();
                gslChunk.getCubes()[(event.getClickedBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16] = gslCube;
            }
            x = event.getClickedBlock().getX() % 16;
            if (event.getClickedBlock().getX() < 0)
                x = Math.abs((-event.getClickedBlock().getX()) % 16 - 15);
            z = event.getClickedBlock().getZ() % 16;
            if (event.getClickedBlock().getZ() < 0)
                z = Math.abs((-event.getClickedBlock().getZ()) % 16 - 15);
            y = (event.getClickedBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) % 16;
            if (gslCube.getNamelayers()[x][y][z] != null && !gslCube.getNamelayers()[x][y][z].getMemberranks().containsKey(event.getPlayer().getUniqueId())) {
                if (event.getClickedBlock().getType().isInteractable()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(new ComponentBuilder("This ", ComponentBuilder.RED).append(event.getClickedBlock().getType().name(), ComponentBuilder.GRAY).append(" is locked.", ComponentBuilder.RED).build());
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){
        for(Block block : new LinkedList<>(event.blockList())){
            GSLChunk gslChunk = GSLChunk.getGSLChunk(block.getChunk());
            GSLCube gslCube = gslChunk.getCubes()[(block.getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
            if (gslCube != null) {
                int x = block.getX() % 16;
                if (block.getX() < 0)
                    x = Math.abs((-block.getX()) % 16 - 15);
                int z = block.getZ() % 16;
                if (block.getZ() < 0)
                    z = Math.abs((-block.getZ()) % 16 - 15);

                int y = (block.getY() - GSLChunk.BLOCK_Y_OFFSET) % 16;
                if (gslCube.getDurability()[x][y][z] > 0) {
                    if (gslCube.getNamelayers()[x][y][z] != null) {
                        gslCube.getDurability()[x][y][z] = gslCube.getDurability()[x][y][z] - 1;
                        if (gslCube.getDurability()[x][y][z] > 0) {

                            int maxdurability = ReinforcementMaterial.getReinforcementMaterialData(gslCube.getReinforcedBy()[x][y][z]).getDurability();
                            double d = (1.0 * gslCube.getDurability()[x][y][z]) / (maxdurability);

                            event.blockList().remove(block);
                            return;
                        }else{
                            gslCube.getNamelayers()[x][y][z] = null;
                            gslCube.getReinforcedBy()[x][y][z] = null;
                            gslCube.getDurability()[x][y][z] = -1;
                        }
                    }
                    gslCube.getNamelayers()[x][y][z] = null;
                    gslCube.getReinforcedBy()[x][y][z] = null;
                    gslCube.getDurability()[x][y][z] = -1;
                }
            }
        }
    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event){
        for(Block block : new LinkedList<>(event.blockList())){
            GSLChunk gslChunk = GSLChunk.getGSLChunk(block.getChunk());
            GSLCube gslCube = gslChunk.getCubes()[(block.getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
            if (gslCube != null) {
                int x = block.getX() % 16;
                if (block.getX() < 0)
                    x = Math.abs((-block.getX()) % 16 - 15);
                int z = block.getZ() % 16;
                if (block.getZ() < 0)
                    z = Math.abs((-block.getZ()) % 16 - 15);

                int y = (block.getY() - GSLChunk.BLOCK_Y_OFFSET) % 16;
                if (gslCube.getDurability()[x][y][z] > 0) {
                    if (gslCube.getNamelayers()[x][y][z] != null) {
                        gslCube.getDurability()[x][y][z] = gslCube.getDurability()[x][y][z] - 1;
                        if (gslCube.getDurability()[x][y][z] > 0) {

                            int maxdurability = ReinforcementMaterial.getReinforcementMaterialData(gslCube.getReinforcedBy()[x][y][z]).getDurability();
                            double d = (1.0 * gslCube.getDurability()[x][y][z]) / (maxdurability);

                            event.blockList().remove(block);
                            return;
                        }else{
                            gslCube.getNamelayers()[x][y][z] = null;
                            gslCube.getReinforcedBy()[x][y][z] = null;
                            gslCube.getDurability()[x][y][z] = -1;
                        }
                    }
                    gslCube.getNamelayers()[x][y][z] = null;
                    gslCube.getReinforcedBy()[x][y][z] = null;
                    gslCube.getDurability()[x][y][z] = -1;
                }
            }
        }
    }
}
