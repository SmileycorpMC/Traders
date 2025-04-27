package net.smileycorp.traders.config;

import com.google.common.collect.Lists;
import net.minecraft.village.MerchantRecipeList;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

import java.util.List;

public class TradeDataLoader {

    public static final TradeDataLoader INSTANCE = new TradeDataLoader();
    
    private final List<TradeTable> tables = Lists.newArrayList();
    
    public MerchantRecipeList getTrades(EntityWanderingTrader trader) {
        TradeContext ctx = new TradeContext(trader);
        MerchantRecipeList trades = new MerchantRecipeList();
        for (TradeTable table : tables) if (table.canApply(ctx)) trades.addAll(table.getTrades(ctx));
        return trades;
    }

}
