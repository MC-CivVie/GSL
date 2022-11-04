package me.zombie_striker.gsl.conversations;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import me.zombie_striker.gsl.prompts.TradingBoothPrompt;
import me.zombie_striker.gsl.utils.ComponentBuilder;

public class TradingBoothConversation {
    private JavaPlugin plugin;
    private ConversationFactory factory;
    
    public void init(JavaPlugin plugin) {
	this.factory = new ConversationFactory(plugin);
	this.plugin = plugin;
    }

    public void convoInit(CommandSender sender) {
	final Map<Object, Object> convMap = new HashMap<Object, Object>();
	convMap.put("null", "Please place down a chest and write the price for each item (in the standard currency for your region). Refer to the wiki for more options. Type 'cancel' to exit.");
	Conversation conv = this.factory
	    .withFirstPrompt(new TradingBoothPrompt())
	    .withInitialSessionData(convMap).withLocalEcho(false)
	    .buildConversation((@NotNull Conversable) sender);
	conv.addConversationAbandonedListener(new ConversationAbandonedListener() {
		@Override
		public void conversationAbandoned(ConversationAbandonedEvent event) {
		    try {
			String boothParameters = (String) event.getContext().getSessionData("data");
			Player sender = (Player) event.getContext().getForWhom();
			Location senderLocation = sender.getLocation();
			Block barrel = senderLocation.getBlock();
			barrel.setType(Material.BARREL);
			var locationOfSign = canPlaceBlock(senderLocation, Material.OAK_SIGN);
			var locationOfIF = canPlaceBlock(senderLocation, Material.ITEM_FRAME);
			if(locationOfSign!=null) {
			    if(locationOfIF!=null) {
				Block sign = locationOfSign.getBlock();
				Block itemf = locationOfIF.getBlock();
				BlockState signstate = sign.getState();
				BlockState itemfstate = itemf.getState();
				Sign signdate = (Sign) signstate;
				ItemFrame itemfdate = (ItemFrame) itemfstate;
				ItemStack itemfitem = new ItemStack(Material.DIAMOND);

				signdate.line(0,new ComponentBuilder("Trading",ComponentBuilder.WHITE).build());
				signdate.line(1,new ComponentBuilder("[xx]",ComponentBuilder.WHITE).build());
				signdate.update();

				itemfdate.setItem(itemfitem);
				itemfdate.setVisible(true);
			    }
			} else {
			    barrel.setType(Material.AIR);
			}
		    } catch(Exception npe) {
			plugin.getLogger().info("Exception happened in TradingBoothConversation.java!\n\n"+npe.getStackTrace());
		    }
		}
	    });
	conv.begin();
    }
    private Location canPlaceBlock(Location location, Material material) {
	if(location.add(1.0,0.0,0.0).getBlock().getType().equals(Material.AIR)) {
	    Block block = location.add(1.0,0.0,0.0).getBlock();
	    block.setType(material);
	    return location.add(1.0,0.0,0.0);
	} else if(location.add(0.0,1.0,0.0).getBlock().getType().equals(Material.AIR)) {
	    Block block = location.add(0.0,1.0,0.0).getBlock();
	    block.setType(material);
	    return location.add(0.0,1.0,0.0);
	} else if(location.add(0.0,0.0,1.0).getBlock().getType().equals(Material.AIR)) {
	    Block block = location.add(0.0,0.0,1.0).getBlock();
	    block.setType(material);
	    return location.add(0.0,0.0,1.0);
	} else if(location.add(-1.0,0.0,0.0).getBlock().getType().equals(Material.AIR)) {
	    Block block = location.add(-1.0,0.0,0.0).getBlock();
	    block.setType(material);
	    return location.add(-1.0,0.0,0.0);
	} else if(location.add(0.0,0.0,-1.0).getBlock().getType().equals(Material.AIR)) {
	    Block block = location.add(0.0,0.0,-1.0).getBlock();
	    block.setType(material);
	    return location.add(0.0,0.0,-1.0);
	} else {
	    return null;
	}
    }
}
