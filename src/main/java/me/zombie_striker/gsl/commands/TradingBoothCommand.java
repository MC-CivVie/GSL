package me.zombie_striker.gsl.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import me.zombie_striker.gsl.conversations.TradingBoothConversation;
import me.zombie_striker.gsl.utils.ComponentBuilder;

public class TradingBoothCommand implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if(sender instanceof Player) {
      if(args.length == 0) {
        sender.sendMessage(new ComponentBuilder("You must provide an argument to this command.",ComponentBuilder.RED).build());
        return false;
      } else {
        switch(args[0]) {
          case "create":
        	TradingBoothConversation tbc = new TradingBoothConversation();
        	tbc.init((JavaPlugin) sender.getServer().getPluginManager().getPlugin("GSL"));
            tbc.convoInit(sender);
            return true;
          default:
            break;
        }
      }
    }
	return false;
  }
}
