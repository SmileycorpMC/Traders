package net.smileycorp.traders.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonConfig {
    
    public static int tickDelay;
    public static int spawnDelay;
    public static int spawnChance;
    public static int spawnChanceIncrement;
    public static int maxSpawnChance;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/traders/spawning.cfg"));
        try {
            tickDelay = config.getInt("tickDelay", "Spawning", 1200, 0, 10000,
                    "How many ticks before the spawn delay updates?");
            spawnDelay = config.getInt("spawnDelay", "Spawning", 24000, 0, Integer.MAX_VALUE,
                    "How many ticks before a trader attempts to spawn?");
            spawnChance = config.getInt("spawnChance", "Spawning", 25, 0, 100,
                    "What is the chance out of 100 for a trader to spawn?");
            spawnChanceIncrement = config.getInt("spawnChanceIncrement", "Spawning", 25, 0, 100,
                    "How much should the spawn chance increase by on each fail?");
            spawnChance = config.getInt("maxSpawnChance", "Spawning", 75, 0, 100,
                    "How high can the spawn chance go?");
        } catch(Exception e) {} finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
