package net.smileycorp.traders.config.item.functions;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.smileycorp.traders.common.TradersLogger;

import java.util.Map;

public class FunctionRegistry {

    public static final FunctionRegistry INSTANCE = new FunctionRegistry();
    
    private final Map<String, ItemFunction.Deserializer> functions;
    
    private FunctionRegistry() {
        functions = Maps.newHashMap();
    }
    
    public void registerFunction(String name, ItemFunction.Deserializer value) {
        if (!functions.containsKey(name)) {
            functions.put(name, value);
            TradersLogger.logInfo("Registered function " + name);
        }
    }
    
    public ItemFunction readFunction(JsonObject json) {
        try {
            if (json.has("function")) return functions.get(json.get("function").getAsString()).apply(json);
        } catch (Exception e) {
            TradersLogger.logError("Failed reading function " + json, e);
        }
        return null;
    }
    
    public void registerDefaultValues() {
        registerFunction("enchant_randomly", EnchantRandomlyFunction::deserialize);
    }
    
}
