package me.zombie_striker.gsl;

import me.zombie_striker.gsl.commands.NamelayerCommand;
import me.zombie_striker.gsl.commands.NamelayerCreateGroupCommand;
import me.zombie_striker.gsl.commands.ReinforceCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class GSLCore extends JavaPlugin {

    @Override
    public void onEnable() {
        new GSL(this).init();
        NamelayerCreateGroupCommand nlcg = new NamelayerCreateGroupCommand();
        getCommand("nlcg").setExecutor(nlcg);

        ReinforceCommand rc = new ReinforceCommand();
        getCommand("r").setExecutor(rc);
        getCommand("r").setTabCompleter(rc);
    }

    @Override
    public void onDisable() {
        GSL.getApi().shutdown();
    }
}
