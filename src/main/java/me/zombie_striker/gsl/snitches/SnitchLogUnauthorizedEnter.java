package me.zombie_striker.gsl.snitches;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public class SnitchLogUnauthorizedEnter extends SnitchLog{

    private Location location;

    public SnitchLogUnauthorizedEnter(Material icon, long date, UUID actor, Location location) {
        super(icon, date, actor);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
