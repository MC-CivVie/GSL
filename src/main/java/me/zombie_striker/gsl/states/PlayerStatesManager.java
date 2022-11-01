package me.zombie_striker.gsl.states;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerStatesManager {

    private static HashMap<UUID, List<PlayerState>> playerstates = new HashMap<>();

    public static void addState(UUID uuid, PlayerState state) {
        if (!playerstates.containsKey(uuid)) {
            playerstates.put(uuid, new LinkedList<>());
        }
        playerstates.get(uuid).add(state);
    }

    public static void removeState(UUID uuid, Class<? extends PlayerState> clazz) {
        if (playerstates.containsKey(uuid)) {
            List<PlayerState> pp = playerstates.get(uuid);
            for (PlayerState e : new LinkedList<>(pp)) {
                if (clazz.isInstance(e)) {
                    pp.remove(e);
                }
            }
        }
    }
    public static List<PlayerState> getStates(UUID uuid){
        return playerstates.containsKey(uuid)?playerstates.get(uuid):new LinkedList<>();
    }
    public static PlayerState getState(UUID uuid, Class<? extends PlayerState> clazz){
        if (playerstates.containsKey(uuid)) {
            List<PlayerState> pp = playerstates.get(uuid);
            for (PlayerState e : new LinkedList<>(pp)) {
                if (clazz.isInstance(e)) {
                    return e;
                }
            }
        }
        return null;
    }
}
