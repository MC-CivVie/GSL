package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.ores.OreObject;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class OreEvents implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        GSLChunk gslChunk = GSLChunk.getGSLChunk(event.getBlock().getChunk());
        GSLCube gslCube = gslChunk.getCubes()[(event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
        if (gslCube != null) {
            int x = event.getBlock().getX() % 16;
            if (event.getBlock().getX() < 0)
                x = Math.abs((-event.getBlock().getX()) % 16 - 15);
            int z = event.getBlock().getZ() % 16;
            if (event.getBlock().getZ() < 0)
                z = Math.abs((-event.getBlock().getZ()) % 16 - 15);

            int y = event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET;

            if (!gslCube.getPlaced()[x][y][z]) {
                randomlyGenerateOres(event.getBlock().getRelative(BlockFace.NORTH));
                randomlyGenerateOres(event.getBlock().getRelative(BlockFace.SOUTH));
                randomlyGenerateOres(event.getBlock().getRelative(BlockFace.EAST));
                randomlyGenerateOres(event.getBlock().getRelative(BlockFace.WEST));
                randomlyGenerateOres(event.getBlock().getRelative(BlockFace.UP));
                randomlyGenerateOres(event.getBlock().getRelative(BlockFace.DOWN));
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        GSLChunk gslChunk = GSLChunk.getGSLChunk(event.getBlock().getChunk());
        GSLCube gslCube = gslChunk.getCubes()[(event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
        if(gslCube==null){
            gslCube = new GSLCube();
        }
        int x = event.getBlock().getX() % 16;
        if (event.getBlock().getX() < 0)
            x = Math.abs((-event.getBlock().getX()) % 16 - 15);
        int z = event.getBlock().getZ() % 16;
        if (event.getBlock().getZ() < 0)
            z = Math.abs((-event.getBlock().getZ()) % 16 - 15);

        int y = event.getBlock().getY() - GSLChunk.BLOCK_Y_OFFSET;
        gslCube.getPlaced()[x][y][z]=true;
    }

    public void randomlyGenerateOres(Block rel) {
        GSLChunk gslChunk = GSLChunk.getGSLChunk(rel.getChunk());
        GSLCube gslCube = gslChunk.getCubes()[(rel.getY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
        if (gslCube != null) {
            int x = rel.getX() % 16;
            if (rel.getX() < 0)
                x = Math.abs((-rel.getX()) % 16 - 15);
            int z = rel.getZ() % 16;
            if (rel.getZ() < 0)
                z = Math.abs((-rel.getZ()) % 16 - 15);

            int y = rel.getY() - GSLChunk.BLOCK_Y_OFFSET;

            if (!gslCube.getPlaced()[x][y][z]) {
                OreObject oreObject = OreObject.getRandomOreForType(rel.getType(),rel.getX(),rel.getY(),rel.getZ());
                if(oreObject!=null){
                    rel.setType(oreObject.getOreBlock());
                }
            }
        }
    }
}
