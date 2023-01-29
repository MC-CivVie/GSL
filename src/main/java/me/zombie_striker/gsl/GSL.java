package me.zombie_striker.gsl;

import me.zombie_striker.gsl.crops.CropType;
import me.zombie_striker.gsl.dependancies.DependancyManager;
import me.zombie_striker.gsl.entities.EntityData;
import me.zombie_striker.gsl.events.*;
import me.zombie_striker.gsl.handlers.BossBarHandler;
import me.zombie_striker.gsl.handlers.CombatTagHandler;
import me.zombie_striker.gsl.handlers.FossilHandler;
import me.zombie_striker.gsl.handlers.PrisonerHandler;
import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.megabuilds.MegaBuild;
import me.zombie_striker.gsl.megabuilds.MegaBuildType;
import me.zombie_striker.gsl.megabuilds.interact.InteractAction;
import me.zombie_striker.gsl.namelayers.NameLayer;
import me.zombie_striker.gsl.ores.OreObject;
import me.zombie_striker.gsl.recipes.FactoryRecipe;
import me.zombie_striker.gsl.reinforcement.ReinforcementMaterial;
import me.zombie_striker.gsl.utils.FileUtils;
import me.zombie_striker.gsl.utils.InternalFileUtil;
import me.zombie_striker.gsl.utils.guis.GUIUtil;
import me.zombie_striker.gsl.wordbank.WordBank;
import me.zombie_striker.gsl.world.GSLBiomeList;
import me.zombie_striker.gsl.world.GSLChunk;
import me.zombie_striker.gsl.world.GSLWorld;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class GSL {

    private static GSLCore core;
    private static GSL api;

    public static GSL getApi() {
        return api;
    }

    public static GSLCore getCore() {
        return core;
    }

    public GSL(GSLCore gslCore) {
        core = gslCore;
        api = this;
    }

    public void init() {
        copyDataFiles();

        // basics
        GSLWorld.init();
        MaterialType.init();
        OreObject.init();
        NameLayer.init();
        FactoryRecipe.init();
        InteractAction.init();
        MegaBuildType.init();
        EntityData.init();
        WordBank.init();
        ReinforcementMaterial.init();
        GUIUtil.init();
        BossBarHandler.init();
        DependancyManager.init();
        GSLBiomeList.init(Bukkit.getWorlds().get(0));
        CropType.init();
        PrisonerHandler.init();
        FossilHandler.init();
        CombatTagHandler.init();


        registerListeners();
        createRunnables();
    }


    public void createRunnables() {
        //Factory ticking
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    GSLWorld gslWorld = GSLWorld.getWorld(world);
                    if (gslWorld != null) {
                        for (MegaBuild megaBuild : gslWorld.getMegabuilds()) {
                            if(megaBuild.isActive()){
                                megaBuild.tick();
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(core, 10,10);
    }


    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new OreEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ChunkLoadEvents(), core);
        Bukkit.getPluginManager().registerEvents(new NamelayerEvents(), core);
        Bukkit.getPluginManager().registerEvents(new SnitchEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ReinforceEvents(), core);
        Bukkit.getPluginManager().registerEvents(new EntityEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ExpEvents(), core);
        Bukkit.getPluginManager().registerEvents(new WordBankEvents(), core);
        Bukkit.getPluginManager().registerEvents(new PlayerChatEvents(), core);
        Bukkit.getPluginManager().registerEvents(new CropEvents(), core);
        Bukkit.getPluginManager().registerEvents(new FactoryEvents(), core);
        Bukkit.getPluginManager().registerEvents(new PrisonEvents(), core);
        Bukkit.getPluginManager().registerEvents(new WorldBorderEvents(), core);
        Bukkit.getPluginManager().registerEvents(new NoteBlockSongEvents(), core);
        Bukkit.getPluginManager().registerEvents(new ElevatorEvents(), core);
        Bukkit.getPluginManager().registerEvents(new FossilEvents(), core);
        Bukkit.getPluginManager().registerEvents(new FishingEvents(), core);
        Bukkit.getPluginManager().registerEvents(new CombatLogEvents(), core);
        Bukkit.getPluginManager().registerEvents(new LivestockEvents(), core);

    }

    public void copyDataFiles() {
        try {
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_ENTITY_DATA), InternalFileUtil.getPathsToInternalFiles("entities"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_MATERIALS_CUSTOM), InternalFileUtil.getPathsToInternalFiles("materials_custom"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_MATERIALS_GROUP), InternalFileUtil.getPathsToInternalFiles("materials_groups"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_ORES), InternalFileUtil.getPathsToInternalFiles("ores"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_REINFORCEMENT_TYPES), InternalFileUtil.getPathsToInternalFiles("reinforcedtypes"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_CROPS), InternalFileUtil.getPathsToInternalFiles("crops"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_RECIPES), InternalFileUtil.getPathsToInternalFiles("recipes"), false);
            InternalFileUtil.copyFilesOut(new File(core.getDataFolder(), FileUtils.PATH_MEGASTRUCTURETYPES), InternalFileUtil.getPathsToInternalFiles("factories"), false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        NameLayer.saveNamelayers();
        PrisonerHandler.save();
        for(World world : Bukkit.getWorlds()){
            for(Chunk chunk : world.getLoadedChunks()){
                GSLChunk.getGSLChunk(chunk).save();
            }
        }
    }
}
