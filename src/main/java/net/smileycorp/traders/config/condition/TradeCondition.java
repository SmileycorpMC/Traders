package net.smileycorp.traders.config.condition;

import net.smileycorp.traders.config.trades.TradeContext;

public interface TradeCondition {
    
    boolean apply(TradeContext ctx);
    
}
