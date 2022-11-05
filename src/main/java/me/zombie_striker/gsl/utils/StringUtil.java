package me.zombie_striker.gsl.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class StringUtil {

    public static Location getLocationFromString(String input){
        String[] args = input.split(",");
        String world = args[0].trim();
        int x = Integer.parseInt(args[1].trim());
        int y = Integer.parseInt(args[2].trim());
        int z = Integer.parseInt(args[3].trim());
        return new Location(Bukkit.getWorld(world),x,y,z);
    }
    public static String getStringLocation(Location location){
        return location.getWorld().getName()+", "+location.getBlockX()+", "+location.getBlockY()+", "+location.getBlockZ();
    }

    public static String formatTime(long time){
        StringBuilder sb = new StringBuilder();
        long t = time;
        boolean first=true;
        while(t >= 1000*60){
            if(!first){
                sb.append(", ");
            }
            if(t >= 1000*60*60*24){
                int days = (int) (t/(1000*60*60*24));
                t%=1000*60*60*24;
                sb.append(days+" days");
                first = false;
                continue;
            }
            if(t >= 1000*60*60){
                int days = (int) (t/(1000*60*60));
                t%=1000*60*60;
                sb.append(days+" hours");
                first = false;
                continue;
            }
            if(t >= 1000*60){
                int days = (int) (t/(1000*60));
                t%=1000*60;
                sb.append(days+" minutes");
                first = false;
                continue;
            }
        }
        return sb.toString();
    }
}
