package me.zombie_striker.gsl.world;

import me.zombie_striker.gsl.megabuilds.MegaBuild;
import me.zombie_striker.gsl.snitches.Snitch;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.LinkedList;
import java.util.List;

public class GSLWorld {

    private static final List<GSLWorld> gslworlds = new LinkedList<>();



    private final List<Snitch> snitches = new LinkedList<>();
    private final List<MegaBuild> megabuilds = new LinkedList<>();

    private World world;

    public static void init(){
        for(World world : Bukkit.getWorlds()){
            gslworlds.add(new GSLWorld(world));
        }
    }
    public static GSLWorld getWorld(World world){
        for(GSLWorld gslWorld : gslworlds){
            if(gslWorld.world==world)
                return gslWorld;
        }
        return null;
    }

    public GSLWorld(World world){
        this.world = world;
    }




    public List<MegaBuild> getMegabuilds() {
        return megabuilds;
    }

    public List<Snitch> getSnitches() {
        return snitches;
    }

    public void registerSnitch(Snitch snitch){
        snitches.add(snitch);
    }
    public void unregisterSnitch(Snitch snitch){
        snitches.remove(snitch);
    }
}
