package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.handlers.FossilHandler;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.ItemUtil;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FossilEvents implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(Math.random() < 0.001) {
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

            int y = (event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) % 16;

            if (!gslCube.getPlaced()[x][y][z]) {
                event.getPlayer().getInventory().addItem(ItemUtil.prepareItem(Material.NAUTILUS_SHELL,"Fossil",new ComponentBuilder("Right click to open.",ComponentBuilder.WHITE).build()));
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem()==null || event.getItem().getType()!=Material.NAUTILUS_SHELL)
            return;
        if(event.getHand()== EquipmentSlot.OFF_HAND)
            return;
        if(event.getAction().isLeftClick())
            return;
        ItemStack hand = event.getItem();
        if(hand.getAmount() == 1){
            hand=null;
        }else {
            hand.setAmount(hand.getAmount() - 1);
        }
        event.getPlayer().getInventory().setItemInMainHand(hand);

        event.getPlayer().getInventory().addItem(FossilHandler.getRandomItem());
    }
}
