package me.zombie_striker.gsl.snitches;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public class SnitchLogPlace extends SnitchLog{

    private Location location;

    public SnitchLogPlace(Material icon, long date, UUID actor, Location location) {
        super(icon, date, actor);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
