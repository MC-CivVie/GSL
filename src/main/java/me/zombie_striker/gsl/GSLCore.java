package me.zombie_striker.gsl;

import me.zombie_striker.gsl.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class GSLCore extends JavaPlugin {

    @Override
    public void onEnable() {
        new GSL(this).init();
        NamelayerCreateGroupCommand nlcg = new NamelayerCreateGroupCommand();
        getCommand("nlcg").setExecutor(nlcg);

        NamelayerMergeCommand nlmergec = new NamelayerMergeCommand();
        getCommand("nlmerge").setExecutor(nlmergec);
        getCommand("nlmerge").setTabCompleter(nlmergec);

        NamelayerDisbandCommand nldiband = new NamelayerDisbandCommand();
        getCommand("nldisband").setExecutor(nldiband);
        getCommand("nldisband").setTabCompleter(nldiband);

        NamelayerInviteCommand nlinvite = new NamelayerInviteCommand();
        getCommand("nlinvite").setExecutor(nlinvite);
        getCommand("nlinvite").setTabCompleter(nlinvite);

        NamelayerAcceptCommand nlaccept = new NamelayerAcceptCommand();
        getCommand("nlaccept").setExecutor(nlaccept);
        getCommand("nlaccept").setTabCompleter(nlaccept);

        ReinforceCommand rc = new ReinforceCommand();
        getCommand("r").setExecutor(rc);
        getCommand("r").setTabCompleter(rc);

        GroupCommand gc = new GroupCommand();
        getCommand("g").setExecutor(gc);
        getCommand("g").setTabCompleter(gc);

        TradingBoothCommand tbc = new TradingBoothCommand();
        getCommand("tb").setExecutor(tbc);
        getCommand("tb").setTabCompleter(tbc);
    }

    @Override
    public void onDisable() {
        GSL.getApi().shutdown();
    }
}
