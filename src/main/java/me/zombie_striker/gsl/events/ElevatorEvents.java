package me.zombie_striker.gsl.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.NotNull;

public class ElevatorEvents implements @NotNull Listener {

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event){
        if(event.getFrom().getBlock().getRelative(BlockFace.DOWN).getType()== Material.GOLD_BLOCK){
            Block block = event.getFrom().getBlock();
            for(int i = block.getY(); i <block.getWorld().getMaxHeight();i++){
                block=block.getRelative(BlockFace.UP);
                if(block.getType()==Material.GOLD_BLOCK){
                    if(block.getRelative(BlockFace.UP).getType().isSolid()||block.getRelative(BlockFace.UP,2).getType().isSolid())
                        continue;
                    event.getPlayer().teleport(block.getRelative(BlockFace.UP).getLocation().add(0.5,0,0.5));
                    event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,2);
                    break;
                }
            }
        }
    }
    @EventHandler
    public void onPlayerShift(PlayerToggleSneakEvent event){
        if(event.isSneaking()) {
            if (event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GOLD_BLOCK) {
                Block block = event.getPlayer().getLocation().getBlock();
                block = block.getRelative(BlockFace.DOWN,3);
                for (int i = block.getY(); i > block.getWorld().getMinHeight(); i--) {
                    block = block.getRelative(BlockFace.DOWN);
                    if (block.getType() == Material.GOLD_BLOCK) {
                        if (block.getRelative(BlockFace.UP).getType().isSolid() || block.getRelative(BlockFace.UP, 2).getType().isSolid())
                            continue;
                        event.getPlayer().teleport(block.getRelative(BlockFace.UP).getLocation().add(0.5,0,0.5));
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
                        break;
                    }
                }
            }
        }
    }
}
