package net.smileycorp.traders.config.condition;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.trades.TradeContext;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

public class ModInstalledCondition implements TradeCondition {
    
    private final Value<String> value;
    
    public ModInstalledCondition(Value<String> value) {
        this.value = value;
    }
    
    @Override
    public boolean apply(TradeContext ctx) {
        return Loader.isModLoaded(value.get(ctx));
    }
    
    public static ModInstalledCondition deserialize(JsonObject json) {
        try {
            return new ModInstalledCondition(ValueRegistry.INSTANCE.readValue(DataType.STRING, json.get("value")));
        } catch(Exception e) {
            TradersLogger.logError("Incorrect parameters for ModCondition", e);
        }
        return null;
    }
    
}
