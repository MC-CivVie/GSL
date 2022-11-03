package me.zombie_striker.gsl.megabuilds;

import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

public class MegaBuild {

    private List<Location> blocks = new LinkedList<>();
    @SuppressWarnings("unused")
	private Location interactBlock;

    public MegaBuild(Location interactBlock, Location... blocks){
        this.interactBlock = interactBlock;
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
