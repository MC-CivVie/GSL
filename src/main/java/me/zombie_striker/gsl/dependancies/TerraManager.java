package me.zombie_striker.gsl.dependancies;

import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Iterator;

public class TerraManager {

    public static Biome getBiomeByLocation(Location location){
        ServerWorld serverWorld = BukkitAdapter.adapt(location.getWorld());
        BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) serverWorld.getBiomeProvider().getBiome(location.getBlockX(),location.getBlockY(),location.getBlockZ(),location.getWorld().getSeed()).getPlatformBiome();
        return platformBiome.getHandle();
    }
    public static Biome getBiomeByName(World world, String name){
        ServerWorld serverWorld = BukkitAdapter.adapt(world);
        Iterator<com.dfsek.terra.api.world.biome.Biome> iter = serverWorld.getBiomeProvider().getBiomes().iterator();

        for(com.dfsek.terra.api.world.biome.Biome b = null; iter.hasNext();){
            b=iter.next();
            if(b.getID().equals(name) || ((Biome)b.getPlatformBiome().getHandle()).name().equals(name)){
                return (Biome) b.getPlatformBiome().getHandle();
            }
        }
        return null;
    }
}
