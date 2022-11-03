package me.zombie_striker.gsl.megabuilds;

import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

public class MegaBuild {

    private List<Location> blocks = new LinkedList<>();
    private Location interactBlock1;

    public MegaBuild(Location interactBlock1, Location... blocks){
        this.interactBlock1 = interactBlock1;
        for(Location l : blocks){
            this.blocks.add(l);
        }
    }

    public List<Location> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Location> blocks) {
        this.blocks = blocks;
    }
}
