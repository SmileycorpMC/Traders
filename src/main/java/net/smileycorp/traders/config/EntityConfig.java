package net.smileycorp.traders.config;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.GameData;
import net.smileycorp.traders.common.TradersLogger;

import java.io.File;
import java.util.List;

public class EntityConfig {

    //wandering trader
    public static int despawnDelay;
    public static int wanderRange;
    public static EntityAttributesEntry trader;
    private static String[] attacksTradersStr;
    private static List<Class<? extends EntityLiving>> attacksTraders;

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
            attacksTradersStr = config.get("Wandering Trader", "attacksTraders", new String[] {"minecraft:zombie", "minecraft:vindication_illager",
                            "minecraft:evocation_illager", "minecraft:illusion_illager", "minecraft:vex", "raids:pillager", "raids:ravager", "tektopia:necromancer"},
                    "Entities that are aggressive to wandering traders, and they flee from.").getStringList();

            //trader llama
            traderLlama = new EntityAttributesEntry(config, "Trader Llama", 0.175, 53, 0, 0, 0, 40);

        } catch (Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

    public static boolean attacksTraders(Entity entity) {
        if (!(entity instanceof EntityLiving)) return false;
        if (attacksTraders == null) {
            attacksTraders = Lists.newArrayList();
            for (String str : attacksTradersStr) try {
                //check if it matches the syntax for a registry name
                if (!str.contains(":")) continue;
                ResourceLocation loc = new ResourceLocation(str);
                if (!GameData.getEntityRegistry().containsKey(loc)) continue;
                Class<?> clazz = GameData.getEntityRegistry().getValue(loc).getEntityClass();
                if (clazz == null) throw new Exception("Entry " + str + " is not in the correct format");
                if (EntityLiving.class.isAssignableFrom(clazz)) {
                    attacksTraders.add((Class<? extends EntityLiving>) clazz);
                    TradersLogger.logInfo("Loaded trader attacker " + clazz + " as " + clazz.getName());
                } else {
                    TradersLogger.logError("Entity " + str + " is not an instance of EntityLiving", new ClassCastException());
                }
            } catch (Exception e) {}
        }
        for (Class<? extends EntityLiving> clazz : attacksTraders) if (clazz.isAssignableFrom(entity.getClass())) return true;
        return false;
    }

}
