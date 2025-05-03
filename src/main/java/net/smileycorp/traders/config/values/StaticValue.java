package net.smileycorp.traders.config.values;

import net.smileycorp.traders.config.trades.TradeContext;

public class StaticValue<T extends Comparable<T>> implements Value<T> {
    
    private final T value;
    
    public StaticValue(T value) {
        this.value = value;
    }
    
    @Override
    public T get(TradeContext ctx) {
        return value;
    }
    
}
