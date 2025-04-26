package net.smileycorp.traders.config.condition;

import net.smileycorp.traders.config.TradeContext;

public interface TradeCondition {
    
    boolean apply(TradeContext ctx);
    
}
