package me.zombie_striker.gsl.events;

import me.zombie_striker.gsl.namelayers.NameLayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class NamelayerEvents implements @NotNull Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(!event.getPlayer().hasPlayedBefore()){
            NameLayer nameLayer = new NameLayer(event.getPlayer().getName(),event.getPlayer().getUniqueId());
            NameLayer.register(nameLayer);
        }
    }
}
