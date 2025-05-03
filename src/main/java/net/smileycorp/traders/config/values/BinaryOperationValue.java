package net.smileycorp.traders.config.values;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.BinaryOperation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.trades.TradeContext;

public class BinaryOperationValue<T extends Number & Comparable<T>> implements Value<T> {
    
    private final BinaryOperation operation;
    private final Value<T> value1, value2;
    
    private BinaryOperationValue(BinaryOperation operation, Value<T> value1, Value<T> value2) {
        this.operation = operation;
        this.value1 = value1;
        this.value2 = value2;
    }
    
    @Override
    public T get(TradeContext ctx) {
        return (T) operation.apply(value1.get(ctx), value2.get(ctx));
    }
    
    public static Deserializer of(BinaryOperation operation) {
        return new Deserializer(operation);
    }
    
    public static class Deserializer {
        
        private final BinaryOperation operation;
        
        private Deserializer(BinaryOperation operation) {
            this.operation = operation;
        }
    
        public <T extends Comparable<T>> BinaryOperationValue deserialize(JsonObject obj, DataType<T> type) {
            Value<T> getter1 = ValueRegistry.INSTANCE.readValue(type, obj.get("value1"));
            Value<T> getter2 = ValueRegistry.INSTANCE.readValue(type, obj.get("value2"));
            if (getter1 == null || getter2 == null | !type.isNumber()) {
                TradersLogger.logError("invalid values for BinaryOperationValue " + operation.getName(), new NullPointerException());
                return null;
            }
            return new BinaryOperationValue(operation, getter1, getter2);
        }
        
    }
    
}
