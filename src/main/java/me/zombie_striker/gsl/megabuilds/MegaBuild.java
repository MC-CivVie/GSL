package me.zombie_striker.gsl.megabuilds;

import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

public class MegaBuild {

    private List<Location> blocks = new LinkedList<>();
    private Location center;
    private MegaBuildType type;

    public MegaBuild(MegaBuildType type, Location center, Location... blocks){
        this.center = center;
        this.type = type;
        for(Location l : blocks){
            this.blocks.add(l);
        }
    }


    public Location getCenter() {
        return center;
    }

    public MegaBuildType getType() {
        return type;
    }

    public List<Location> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Location> blocks) {
        this.blocks = blocks;
    }
}
