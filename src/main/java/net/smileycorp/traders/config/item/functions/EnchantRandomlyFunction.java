package net.smileycorp.traders.config.item.functions;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.trades.TradeContext;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

public class EnchantRandomlyFunction implements ItemFunction {
    
    private static final EnchantRandomly FUNCTION = new EnchantRandomly(new LootCondition[0], null);
    
    private final Value<Integer> level;
    
    private EnchantRandomlyFunction(Value<Integer> level) {
        this.level = level;
    }
    
    @Override
    public ItemStack apply(ItemStack stack, TradeContext ctx) {
        return FUNCTION.apply(stack, ctx.getTrader().getRNG(), null);
    }
    
    public static EnchantRandomlyFunction deserialize(JsonObject json) {
        try {
            return new EnchantRandomlyFunction(ValueRegistry.INSTANCE.readValue(DataType.INT, json.get("level")));
        } catch(Exception e) {
            TradersLogger.logError("Incorrect parameters for GameDifficultyCondition", e);
        }
        return null;
    }
    
}
