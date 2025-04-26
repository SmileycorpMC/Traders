package net.smileycorp.traders.config.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.atlas.api.data.UnaryOperation;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;

public class UnaryOperationValue<T extends Number & Comparable<T>> implements Value<T> {
    
    private final UnaryOperation operation;
    private final Value<T> value;
    
    private UnaryOperationValue(UnaryOperation operation, Value<T> value) {
        this.operation = operation;
        this.value = value;
    }
    
    @Override
    public T get(TradeContext ctx) {
        return (T) operation.apply(value.get(ctx));
    }
    
    public static Deserializer of(UnaryOperation operation) {
        return new Deserializer(operation);
    }
    
    public static class Deserializer {
        
        private final UnaryOperation operation;
        
        private Deserializer(UnaryOperation operation) {
            this.operation = operation;
        }
    
        public <T extends Comparable<T>> UnaryOperationValue deserialize(JsonObject element, DataType<T> type) {
            Value getter = ValueRegistry.INSTANCE.readValue(type, element.get("value"));
            if (getter == null |! type.isNumber()) {
                TradersLogger.logError("invalid value for UnaryOperationValue " + operation.getName(), new NullPointerException());
                return null;
            }
            return new UnaryOperationValue(operation, getter);
        }
        
    }
    
}
