package me.zombie_striker.gsl.snitches;

import org.bukkit.Location;

public class Snitch {

    public static final int SNITCHLOG_SIZE = 54;
    public static final int SNITCH_RANGE=16;

    private SnitchLog[] snitchlog;
    private Location location;

    public Snitch(Location location,int size){
        this.location = location;
        this.snitchlog = new SnitchLog[size];
    }

    public Location getLocation() {
        return location;
    }

    public SnitchLog[] getSnitchlog() {
        return snitchlog;
    }

    public boolean canDetect(Location location) {
        if(Math.abs(location.getBlockX()-location.getBlockX()) < SNITCH_RANGE){
            if(Math.abs(location.getBlockY()-location.getBlockY()) < SNITCH_RANGE){
                if(Math.abs(location.getBlockZ()-location.getBlockZ()) < SNITCH_RANGE){
                    return true;
                }
            }
        }
        return false;
    }

    public void addToLog(SnitchLog placelog) {
        for(int i = snitchlog.length-2; i >= 0; i--){
            snitchlog[i+1]=snitchlog[i];
        }
        snitchlog[0]=placelog;
    }
}
