package me.zombie_striker.gsl.handlers;

import me.zombie_striker.gsl.GSL;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class BossBarHandler {

    private static HashMap<UUID, BossBar> bossbars = new HashMap<>();
    private static HashMap<UUID, Long> lastUpdated = new HashMap<>();

    public static void init() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, BossBar> e : new HashSet<>(bossbars.entrySet())) {
                    if ((lastUpdated.containsKey(e.getKey()) && (System.currentTimeMillis() - lastUpdated.get(e.getKey())) > 1000 * 25)) {
                        bossbars.remove(e.getKey());
                        Player player;
                        if ((player = Bukkit.getPlayer(e.getKey())) != null)
                            e.getValue().removePlayer(player);
                    }
                }
            }
        }.runTaskTimer(GSL.getCore(), 20 * 5, 20 * 5);
    }

    public static void removeBossbar(UUID uuid){
        BossBar bossBar=bossbars.get(uuid);
        if(bossBar!=null) {
            Player player;
            if ((player = Bukkit.getPlayer(uuid)) != null)
                bossBar.removePlayer(player);

            bossbars.remove(uuid);
            lastUpdated.remove(uuid);
        }
    }

    public static void setBossbarsStats(Player player, String text, double percentage, BarColor color) {
        lastUpdated.put(player.getUniqueId(), System.currentTimeMillis());
        BossBar bossBar;
        if (!bossbars.containsKey(player.getUniqueId())) {
            bossBar = Bukkit.createBossBar(text, color, BarStyle.SEGMENTED_20);
            bossBar.setProgress(percentage);
            bossBar.addPlayer(player);
            bossbars.put(player.getUniqueId(), bossBar);
        } else {
            bossBar = bossbars.get(player.getUniqueId());
            bossBar.setTitle(text);
            bossBar.setColor(color);
            bossBar.setProgress(percentage);
            bossbars.put(player.getUniqueId(), bossBar);
        }
    }
}
