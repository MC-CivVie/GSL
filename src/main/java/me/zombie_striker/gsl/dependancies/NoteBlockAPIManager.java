package me.zombie_striker.gsl.dependancies;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class NoteBlockAPIManager {

    private static HashMap<SongPlayer, Location> locationOfSongs = new HashMap<>();

    public static boolean alreadyPlayingSong(Location location) {
        for (Map.Entry<SongPlayer, Location> e : locationOfSongs.entrySet()) {
            if (e.getValue().equals(location))
                return true;
        }
        return false;
    }

    public static SongPlayer getSongAt(Location location) {
        for (Map.Entry<SongPlayer, Location> e : locationOfSongs.entrySet()) {
            if (e.getValue().equals(location))
                return e.getKey();
        }
        return null;
    }

    public static boolean playNoteBlockSong(String midi, Location spot) {
        File folder = FileUtils.getFolder(FileUtils.PATH_SONGS);
        if (!folder.exists()) {
            folder.mkdirs();
            return false;
        }
        File file = new File(folder, midi + ".nbs");
        if (!file.exists())
            return false;
        Song song = NBSDecoder.parse(file);
        SongPlayer songPlayer = new RadioSongPlayer(song);
        songPlayer.setAutoDestroy(true);
        List<Player> nearby = new LinkedList<>();

        for (Player player : spot.getWorld().getPlayers())
            if (player.getLocation().distanceSquared(spot) < 50 * 50)
                nearby.add(player);

        for (Player player : nearby)
            songPlayer.addPlayer(player);
        songPlayer.setPlaying(true);
        locationOfSongs.put(songPlayer,spot);
        return true;
    }

    public static void updateNearby(Player player) {
        for (Map.Entry<SongPlayer, Location> e : locationOfSongs.entrySet()) {
            if (player.getLocation().distanceSquared(e.getValue()) < 50 * 50) {
                if (!e.getKey().getPlayerUUIDs().contains(player.getUniqueId()))
                    e.getKey().addPlayer(player);
            } else {
                if (e.getKey().getPlayerUUIDs().contains(player.getUniqueId()))
                    e.getKey().removePlayer(player);

            }
        }
    }

    public static void stopSong(Location location) {
        RadioSongPlayer sp = (RadioSongPlayer) NoteBlockAPIManager.getSongAt(location);
        sp.setPlaying(false);
        sp.destroy();
        for (UUID player : sp.getPlayerUUIDs()) {
            sp.removePlayer(player);
        }
        locationOfSongs.remove(sp);
    }
}
