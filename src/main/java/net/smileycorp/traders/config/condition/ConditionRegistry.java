package net.smileycorp.traders.config.condition;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.LogicalOperation;
import net.smileycorp.traders.common.TradersLogger;

import java.util.Map;
import java.util.function.Function;

public class ConditionRegistry {

    public static final ConditionRegistry INSTANCE = new ConditionRegistry();
    
    private final Map<String, Function<JsonObject, TradeCondition>> conditions;
    
    private ConditionRegistry() {
        conditions = Maps.newHashMap();
    }
    
    public void registerCondition(String name, Function<JsonObject, TradeCondition> condition) {
        if (!conditions.containsKey(name)) {
            TradersLogger.logInfo("Registered condition " + name);
            conditions.put(name, condition);
        }
    }
    
    public TradeCondition readCondition(JsonObject json) {
        try {
            if (json.has("condition")) return conditions.get(json.get("condition").getAsString()).apply(json);
        } catch (Exception e) {
            TradersLogger.logError("Failed reading condition " + json, e);
        }
        return null;
    }
    
    public void registerDefaultConditions() {
        for (LogicalOperation operation : LogicalOperation.values())
            registerCondition(operation.getName(), e -> LogicalCondition.deserialize(operation, e));
        registerCondition("not", NotCondition::deserialize);
        registerCondition("comparison", ComparisonCondition::deserialize);
        registerCondition("random", RandomCondition::deserialize);
        registerCondition("biome", BiomeCondition::deserialize);
        registerCondition("local_difficulty", LocalDifficultyCondition::deserialize);
        registerCondition("game_difficulty", GameDifficultyCondition::deserialize);
        registerCondition("mod_installed", ModInstalledCondition::deserialize);
    }
    
}
