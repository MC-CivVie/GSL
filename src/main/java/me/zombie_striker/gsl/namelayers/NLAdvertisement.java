package me.zombie_striker.gsl.namelayers;

import org.bukkit.Material;

import java.util.List;

public class NLAdvertisement {

    private String displayname;
    private String ideology;
    private String lore;
    private String discord;
    private Material icon;

    public NLAdvertisement(String displayname, String ideology, String lore, String discord, Material icon){
        this.discord=discord;
        this.displayname=displayname;
        this.lore=lore;
        this.ideology=ideology;
        this.icon = icon;
    }

    public Material getIcon() {
        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getDiscord() {
        return discord;
    }

    public String getIdeology() {
        return ideology;
    }

    public String getLore() {
        return lore;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setIdeology(String ideology) {
        this.ideology = ideology;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }
}
