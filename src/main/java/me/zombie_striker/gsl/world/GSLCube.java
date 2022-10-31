package me.zombie_striker.gsl.world;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;

public class GSLCube {

    private int[][][] durability = new int[16][16][16];
    private MaterialType[][][] reinforcedBy = new MaterialType[16][16][16];
    private NameLayer[][][] namelayers = new NameLayer[16][16][16];
    private boolean[][][] placed = new boolean[16][16][16];

    public GSLCube(){

    }

    public int[][][] getDurability() {
        return durability;
    }

    public MaterialType[][][] getReinforcedBy() {
        return reinforcedBy;
    }

    public NameLayer[][][] getNamelayers() {
        return namelayers;
    }

    public void setDurability(int x,int y, int z, int durability) {
        this.durability[x][y][z]= durability;
    }

    public void setNamelayers(int x,int y, int z,NameLayer namelayers) {
        this.namelayers[x][y][z]= namelayers;
    }

    public void setReinforcedBy(int x,int y, int z,MaterialType reinforcedBy) {
        this.reinforcedBy[x][y][z]= reinforcedBy;
    }
    public void setPlaced(int x,int y, int z,boolean placed) {
        this.placed[x][y][z]= placed;
    }

    public boolean[][][] getPlaced() {
        return placed;
    }

}
