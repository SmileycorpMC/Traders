package net.smileycorp.traders.config.values;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.atlas.api.recipe.WeightedOutputs;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;

import java.util.Map;

public class WeightedRandomValue<T extends Comparable<T>> implements Value<T> {
    
    private final WeightedOutputs<Value<T>> outputs;
    
    public WeightedRandomValue(WeightedOutputs<Value<T>> outputs) {
        this.outputs = outputs;
    }
    
    @Override
    public T get(TradeContext ctx) {
        return outputs.getResult(ctx.getTrader().getRNG()).get(ctx);
    }
    
    public static <T extends Comparable<T>> WeightedRandomValue deserialize(JsonObject json, DataType<T> type) {
        Map<Value<T>, Integer> values = Maps.newHashMap();
        try {
            if (type.isNumber()) throw new ClassCastException();
            for (JsonElement element : json.get("value").getAsJsonArray()) {
                try {
                    JsonObject entry = element.getAsJsonObject();
                    Value<T> getter = ValueRegistry.INSTANCE.readValue(type, entry.get("value"));
                    if (getter != null) values.put(getter, entry.get("weight").getAsInt());
                } catch (Exception e) {
                    TradersLogger.logError("invalid entry for " + element + " for WeightedRandomValue", e);
                }
            }
        } catch (Exception e) {
            TradersLogger.logError("invalid value for WeightedRandomValue", e);
        }
        return new WeightedRandomValue(new WeightedOutputs(values));
    }
    
}
