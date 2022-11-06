package me.zombie_striker.gsl.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.zombie_striker.gsl.states.PlayerState;
import me.zombie_striker.gsl.states.PlayerStatesManager;
import me.zombie_striker.gsl.states.TalkInGroupState;
import me.zombie_striker.gsl.utils.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerChatEvents implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event){
        event.setCancelled(true);
        PlayerState ps = PlayerStatesManager.getState(event.getPlayer().getUniqueId(), TalkInGroupState.class);
        if(ps==null){
            ComponentBuilder chatmessage = new ComponentBuilder("[!]"+event.getPlayer().getName()+": ",ComponentBuilder.GRAY);
            chatmessage.append(((TextComponent)event.message()).content(),ComponentBuilder.WHITE);
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(chatmessage.build());
            }
        }else{
            if(ps instanceof TalkInGroupState) {
                ComponentBuilder chatmessage = new ComponentBuilder("[" + ((TalkInGroupState) ps).getChatGroup().getName() + "]" + event.getPlayer().getName() + ": ", ComponentBuilder.GRAY);
                chatmessage.append(((TextComponent) event.message()).content(), ComponentBuilder.WHITE);
                for (UUID uuid : ((TalkInGroupState) ps).getChatGroup().getMemberranks().keySet()) {
                    Player p = Bukkit.getPlayer(uuid);
                    if(p != null)
                    p.sendMessage(chatmessage.build());
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.quitMessage(new ComponentBuilder("",ComponentBuilder.WHITE).build());
    }
}
