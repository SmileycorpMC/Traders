package net.smileycorp.traders.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class SpawnConfig {

    //wandering trader
    public static boolean tradersSpawn;
    public static int tickDelay;
    public static int spawnDelay;
    public static int spawnChance;
    public static int spawnChanceIncrement;
    public static int maxSpawnChance;
    public static int spawnPositionChecks;

    //trader llama
    public static int minLlamas;
    public static int maxLlamas;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/traders/spawning.cfg"));
        try {
            //wandering trader
            tradersSpawn = config.getBoolean("tradersSpawn", "Wandering Trader", true,
                    "Should traders naturally spawn like vanilla 1.14+?");
            tickDelay = config.getInt("tickDelay", "Wandering Trader", 1200, 0, 10000,
                    "How many ticks before the spawn delay updates?");
            spawnDelay = config.getInt("spawnDelay", "Wandering Trader", 24000, 0, Integer.MAX_VALUE,
                    "How many ticks before a trader attempts to spawn?");
            spawnChance = config.getInt("spawnChance", "Wandering Trader", 25, 0, 100,
                    "What is the chance out of 100 for a trader to spawn?");
            spawnChanceIncrement = config.getInt("spawnChanceIncrement", "Wandering Trader", 25, 0, 100,
                    "How much should the spawn chance increase by on each fail?");
            spawnChance = config.getInt("maxSpawnChance", "Wandering Trader", 75, 0, 100,
                    "How high can the spawn chance go?");
            spawnPositionChecks = config.getInt("spawnPositionChecks", "Wandering Trader", 10, 0, Integer.MAX_VALUE,
                    "How many checks should traders do to find a valid position to spawn?");
            //trader llama
            minLlamas = config.getInt("minLlamas", "Trader LLama", 2, 0, Integer.MAX_VALUE,
                    "Minimum number of trader llamas to spawn with wandering traders?");
            maxLlamas = config.getInt("maxLlamas", "Trader LLama", 2, 0, Integer.MAX_VALUE,
                    "Maximum number of trader llamas to spawn with wandering traders?");
        } catch(Exception e) {} finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
