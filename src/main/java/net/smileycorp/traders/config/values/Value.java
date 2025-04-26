package net.smileycorp.traders.config.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.config.TradeContext;

public interface Value<T extends Comparable<T>> {
    
    T get(TradeContext ctx);
    
    interface Deserializer {
        
        <T extends Comparable<T>> Value<T> apply(JsonObject obj, DataType<T> type);
        
    }
    
}
