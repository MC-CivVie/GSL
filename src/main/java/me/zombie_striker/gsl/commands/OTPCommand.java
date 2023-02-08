package me.zombie_striker.gsl.commands;

import me.zombie_striker.gsl.utils.ComponentBuilder;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class OTPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        File file = FileUtils.getFolder(FileUtils.PATH_OTP);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;


        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        if(fc.contains("opt")){
            for(String uuid2 : fc.getStringList("opt")){
                UUID uuid = UUID.fromString(uuid2);
                if(uuid.equals(player.getUniqueId()))
                    return true;
            }
        }

        if(args.length == 0){
            player.sendMessage(new ComponentBuilder("You need to specify who you wish to TP to.",ComponentBuilder.RED).build());
            return true;
        }
        Player who = Bukkit.getPlayer(args[0]);
        if(who!=null){
            player.teleport(who);
            List<String> uuids = new LinkedList<>();
            if(fc.contains("opt")){
                uuids = fc.getStringList("opt");
            }
            uuids.add(player.getUniqueId().toString());
            fc.set("opt",uuids);
            try {
                fc.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

}
