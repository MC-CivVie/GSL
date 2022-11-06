package me.zombie_striker.gsl.snitches;

import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLCube;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Snitch {

    public static final int SNITCHLOG_SIZE = 54;
    public static final int SNITCH_RANGE = 16;

    private SnitchLog[] snitchlog;
    private Location location;

    public Snitch(Location location, int size) {
        this.location = location;
        this.snitchlog = new SnitchLog[size];
    }

    public Location getLocation() {
        return location;
    }

    public SnitchLog[] getSnitchlog() {
        return snitchlog;
    }

    public NameLayer getNameLayer() {
        GSLChunk gslChunk = GSLChunk.getGSLChunk(location.getChunk());
        GSLCube gslCube = gslChunk.getCubes()[(location.getBlockY() - GSLChunk.BLOCK_Y_OFFSET) / 16];
        if (gslCube != null) {
            int x = location.getBlockX() % 16;
            if (location.getBlockX() < 0)
                x = Math.abs((-location.getBlockX()) % 16 - 15);
            int z = location.getBlockZ() % 16;
            if (location.getBlockZ() < 0)
                z = Math.abs((-location.getBlockZ()) % 16 - 15);

            int y = (location.getBlockY() - GSLChunk.BLOCK_Y_OFFSET) % 16;

            if (gslCube.getNamelayers()[x][y][z] != null) {
                return gslCube.getNamelayers()[x][y][z];
            }
        }
        return null;
    }

    public void broadcast(Component message){
        for(UUID uuid: getNameLayer().getMemberranks().keySet()){
            Player player = Bukkit.getPlayer(uuid);
            if(player!=null){
                player.sendMessage(message);
            }
        }
    }

    public boolean canDetect(Location location, Player actor) {
        if (getNameLayer().getMemberranks().containsKey(actor.getUniqueId()))
            return false;

        if (Math.abs(location.getBlockX() - location.getBlockX()) < SNITCH_RANGE) {
            if (Math.abs(location.getBlockY() - location.getBlockY()) < SNITCH_RANGE) {
                if (Math.abs(location.getBlockZ() - location.getBlockZ()) < SNITCH_RANGE) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addToLog(SnitchLog placelog) {
        for (int i = snitchlog.length - 2; i >= 0; i--) {
            snitchlog[i + 1] = snitchlog[i];
        }
        snitchlog[0] = placelog;
    }
}
