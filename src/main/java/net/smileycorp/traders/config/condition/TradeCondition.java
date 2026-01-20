package net.smileycorp.traders.config.condition;

import net.smileycorp.traders.config.trades.TradeContext;

import java.util.List;

public interface TradeCondition {
    
    boolean apply(TradeContext ctx);

    default boolean addToJEI() {
        return true;
    }
    
}
