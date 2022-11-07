package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.data.Pair;
import me.zombie_striker.gsl.namelayers.NameLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NamelayerInviteHandler {

    private static HashMap<Pair<UUID,NameLayer>, Byte> namelayers = new HashMap<>();

    public static boolean isAccepting(UUID player, NameLayer nameLayer){
        for(Map.Entry<Pair<UUID, NameLayer>, Byte> e : namelayers.entrySet()){
            if(e.getKey().getFirst().equals(player)&&e.getKey().getSecond().equals(nameLayer))
                return true;
        }
        return false;
    }
    public static byte getRankForInvite(UUID player, NameLayer nameLayer){
        for(Map.Entry<Pair<UUID, NameLayer>, Byte> e : namelayers.entrySet()){
            if(e.getKey().getFirst().equals(player)&&e.getKey().getSecond().equals(nameLayer))
                return e.getValue();
        }
        return -1;
    }
    public static void invite(UUID uuid, NameLayer nameLayer, byte rank){
        namelayers.put(new Pair<>(uuid,nameLayer),rank);
    }
}
