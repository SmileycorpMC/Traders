package net.smileycorp.traders.config.condition;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

import java.util.List;

public class BiomeCondition implements TradeCondition {
    
    protected final List<Value<String>> biomes;
    
    public BiomeCondition(List<Value<String>> biomes) {
        this.biomes = biomes;
    }
    
    @Override
    public boolean apply(TradeContext ctx) {
        Biome biome = ctx.getWorld().getBiome(ctx.getTrader().getPosition());
        for (Value<String> value : biomes) {
            String str = value.get(ctx);
            try {
                Biome check = ForgeRegistries.BIOMES.getValue(new ResourceLocation(str));
                if (check != null) {
                    if (check == biome) return true;
                    continue;
                }
            } catch (Exception e) {}
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.getType(str))) return true;
        }
        return false;
    }
    
    public static BiomeCondition deserialize(JsonObject json) {
        try {
            if (json.isJsonArray()) {
                List<Value<String>> biomes = Lists.newArrayList();
                for (JsonElement element : json.get("value").getAsJsonArray()) biomes.add(ValueRegistry.INSTANCE.readValue(DataType.STRING, element));
                return new BiomeCondition(biomes);
            }
            return new BiomeCondition(Lists.newArrayList(ValueRegistry.INSTANCE.readValue(DataType.STRING, json)));
        } catch(Exception e) {
            TradersLogger.logError("Incorrect parameters for BiomeCondition", e);
        }
        return null;
    }
    
}
