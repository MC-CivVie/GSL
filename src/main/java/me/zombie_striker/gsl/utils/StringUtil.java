package me.zombie_striker.gsl.utils;

public class StringUtil {

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
