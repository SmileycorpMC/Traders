package net.smileycorp.traders.config.values;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.BinaryOperation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.atlas.api.data.UnaryOperation;
import net.smileycorp.traders.common.TradersLogger;

import java.util.Map;

public class ValueRegistry {

    public static final ValueRegistry INSTANCE = new ValueRegistry();
    
    private final Map<String, Value.Deserializer> values;
    
    private ValueRegistry() {
        values = Maps.newHashMap();
    }
    
    public <T extends Comparable<T>> void registerValue(String name, Value.Deserializer value) {
        if (!values.containsKey(name)) {
            values.put(name, value);
            TradersLogger.logInfo("Registered value " + name);
        }
    }
    
    public <T extends Comparable<T>> Value<T> readValue(DataType<T> type, JsonElement json) {
        if (json == null) return new EmptyValue<>(type);
        if (json.isJsonPrimitive()) return new StaticValue(type.readFromJson(json));
        if (!json.isJsonObject()) return new EmptyValue<>(type);
        JsonObject obj = json.getAsJsonObject();
        try {
            if (obj.has("name")) return values.get(obj.get("name").getAsString()).apply(obj, type);
        } catch (Exception e) {
            TradersLogger.logError("Failed reading value " + obj, e);
        }
        return new EmptyValue<>(type);
    }
    
    public void registerDefaultValues() {
        UnaryOperation.values().forEach(operation -> registerValue(operation.getName(), UnaryOperationValue.of(operation)::deserialize));
        BinaryOperation.values().forEach(operation -> registerValue(operation.getName(), BinaryOperationValue.of(operation)::deserialize));
        registerValue("conditional", ConditionalValue::deserialize);
        registerValue("weighted_random", WeightedRandomValue::deserialize);
        registerValue("level_nbt", LevelNBTValue::deserialize);
        registerValue("trader_nbt", TraderNBTValue::deserialize);
        registerValue("item_1_count", Item1CountValue::deserialize);
    }
    
}
