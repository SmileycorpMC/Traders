package net.smileycorp.traders.config.values;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;
import net.smileycorp.traders.config.condition.ConditionRegistry;
import net.smileycorp.traders.config.condition.TradeCondition;

import java.util.List;

public class ConditionalValue<T extends Comparable<T>> implements Value<T> {
    
    private final Value<T> value, defaultValue;
    private final List<TradeCondition> conditions = Lists.newArrayList();
    
    public ConditionalValue(Value<T> value, Value<T> value2, List<TradeCondition> conditions) {
        this.value = value;
        this.defaultValue = value2;
        this.conditions.addAll(conditions);
    }
    
    @Override
    public T get(TradeContext ctx) {
        for (TradeCondition condition : conditions) if (!condition.apply(ctx)) return defaultValue.get(ctx);
        return value.get(ctx);
    }
    
    public static <T extends Comparable<T>> ConditionalValue deserialize(JsonObject obj, DataType<T> type) {
        Value<T> value = ValueRegistry.INSTANCE.readValue(type, obj.get("value"));
        Value<T> defaultValue = ValueRegistry.INSTANCE.readValue(type, obj.get("default"));
        List<TradeCondition> conditions = Lists.newArrayList();
        if (value == null || defaultValue == null) {
            TradersLogger.logError("invalid values for ConditionalValue", new NullPointerException());
            return null;
        }
        if (obj.has("conditions")) for (JsonElement element : obj.get("conditions").getAsJsonArray()) {
            TradeCondition condition = ConditionRegistry.INSTANCE.readCondition(element.getAsJsonObject());
            if (condition != null) conditions.add(condition);
        }
        return new ConditionalValue(value, defaultValue, conditions);
    }
    
}
