package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.GSL;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class CombatTagHandler {

    private static HashMap<UUID,Long> lastAttack = new HashMap<>();

    public static void init(){
            new BukkitRunnable(){
                @Override
                public void run() {
                    for(Map.Entry<UUID, Long> e : new LinkedList<>(lastAttack.entrySet())){
                        if(System.currentTimeMillis()-e.getValue()>1000*30){
                            lastAttack.remove(e.getKey());
                            continue;
                        }
                        Player player = Bukkit.getPlayer(e.getKey());
                        if(player==null){
                            continue;
                        }
                        double time = System.currentTimeMillis()-e.getValue();
                        time/=30*1000;
                        BossBarHandler.setBossbarsStats(player,"CombatLogger: Do not log off!",1.0-time,BarColor.RED);
                    }
                }
            }.runTaskTimer(GSL.getCore(),20,20);
    }

    public static void addTag(UUID player){
        lastAttack.put(player,System.currentTimeMillis());
    }
    public static long getTagTime(UUID uuid){
        return lastAttack.get(uuid);
    }
    public static boolean isTagged(UUID uuid){
        return lastAttack.containsKey(uuid);
    }
}
