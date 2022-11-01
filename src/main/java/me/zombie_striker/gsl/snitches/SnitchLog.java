package me.zombie_striker.gsl.snitches;

import org.bukkit.Material;

import java.util.UUID;

public abstract class SnitchLog {

    private Material icon;
    private long date;
    private UUID actor;

    public SnitchLog(Material icon, long date, UUID actor){
        this.icon = icon;
        this.date = date;
        this.actor=actor;
    }

    public long getDate() {
        return date;
    }

    public Material getIcon() {
        return icon;
    }

    public UUID getActor() {
        return actor;
    }

    public void setActor(UUID actor) {
        this.actor = actor;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }
}
