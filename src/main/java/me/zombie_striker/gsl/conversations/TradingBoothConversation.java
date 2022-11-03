package me.zombie_striker.gsl.conversations;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import me.zombie_striker.gsl.prompts.TradingBoothPrompt;

public class TradingBoothConversation {
  private JavaPlugin plugin;
  private ConversationFactory factory;

  public void init(JavaPlugin plugin) {
    this.factory = new ConversationFactory(plugin);
    this.plugin = plugin;
  }

  public void convoInit(CommandSender sender) {
    final Map<Object, Object> convMap = new HashMap<Object, Object>();
    convMap.put("null", "Please place down a chest and write the price for each item (in the standard currency). Refer to the wiki for more options. Type 'cancel' to exit.");
    Conversation conv = this.factory
      .withFirstPrompt(new TradingBoothPrompt())
      .withInitialSessionData(convMap).withLocalEcho(false)
      .buildConversation((@NotNull Conversable) sender);
      conv.addConversationAbandonedListener(new ConversationAbandonedListener() {

		@Override
        public void conversationAbandoned(ConversationAbandonedEvent event) {
          try {
            // TODO: parse the booth params here.
            String boothParameters = (String) event.getContext().getSessionData("data");
            Player sender = (Player) event.getContext().getForWhom();
            sender.chat(boothParameters);
          } catch(NullPointerException npe) {
            plugin.getLogger().info("NullPointerException happened in TradingBoothConversation.java!\n\n"+npe.getStackTrace());
          }
        }
      });
      conv.begin();
  }
}
