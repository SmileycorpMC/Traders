package net.smileycorp.traders.config.values;

import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.config.trades.TradeContext;

public class EmptyValue<T extends Comparable<T>> implements Value<T> {
    
    private final DataType<T> type;
    
    public EmptyValue(DataType<T> type) {
        this.type = type;
    }
    
    @Override
    public T get(TradeContext ctx) {
        return type.getDefaultValue();
    }
    
}
