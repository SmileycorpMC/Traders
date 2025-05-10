package net.smileycorp.traders.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ClientConfig {
    
    public static boolean silentTraders;
    public static float traderVolume;
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/traders/client.cfg"));
        try {
            silentTraders = config.getBoolean("silentTraders", "Client", false,
                    "Should traders make noise?");
            traderVolume = config.getFloat("traderVolume", "Client", 1, 0, 10,
                    "How loud should trader sounds play? (1 is default volume.)");
        } catch(Exception e) {} finally {
            if (config.hasChanged()) config.save();
        }
    }
    
}
