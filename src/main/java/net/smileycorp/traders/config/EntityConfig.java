package net.smileycorp.traders.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class EntityConfig {

    //wandering trader
    public static int despawnDelay;
    public static int wanderRange;
    public static EntityAttributesEntry trader;

    //trader llama
    public static EntityAttributesEntry traderLlama;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/traders/entities.cfg"));
        try {
            //wandering trader
            despawnDelay = config.getInt("despawnDelay", "Wandering Trader", 48000, 0, Integer.MAX_VALUE,
                    "How long should wandering traders exist for before despawning?");
            wanderRange = config.getInt("wanderRange", "Wandering Trader", 16, 0, Integer.MAX_VALUE,
                    "How far can wandering traders wander from their home point?");
            trader = new EntityAttributesEntry(config, "Wandering Trader", 0.7, 20, 0, 0, 0, 16);

            //trader llama
            traderLlama = new EntityAttributesEntry(config, "Trader Llama", 0.175, 53, 0, 0, 0, 40);

        } catch (Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
