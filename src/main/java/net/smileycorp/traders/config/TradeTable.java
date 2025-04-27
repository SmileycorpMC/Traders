package net.smileycorp.traders.config;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.village.MerchantRecipe;
import net.smileycorp.traders.config.condition.ConditionRegistry;
import net.smileycorp.traders.config.condition.TradeCondition;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TradeTable {
    
    private final int min, max;
    private final List<TradeCondition> conditions;
    private final List<Trade> trades;
    
    private TradeTable(JsonObject json) throws Exception {
        min = json.has("min") ? json.get("min").getAsInt() : 1;
        max = json.has("max") ? json.get("max").getAsInt() : 1;
        if (max < min) throw new IndexOutOfBoundsException("max cannot be less than min");
        conditions = Lists.newArrayList();
        if (json.has("conditions")) for (JsonElement element : json.get("conditions").getAsJsonArray())
            conditions.add(ConditionRegistry.INSTANCE.readCondition(element.getAsJsonObject()));
        trades = Lists.newArrayList();
        if (!json.has("trades")) throw new NullPointerException("table must specify \"trades\" property");
        for (JsonElement element : json.get("trades").getAsJsonArray()) {
            Trade trade = Trade.deserialize(element);
            if (trade != null) trades.add(trade);
        }
    }
    
    public boolean canApply(TradeContext ctx) {
        for (TradeCondition condition : conditions) if (!condition.apply(ctx)) return false;
        return true;
    }
    
    public Collection<MerchantRecipe> getTrades(TradeContext ctx) {
        List<MerchantRecipe> recipes = Lists.newArrayList();
        Random rand = ctx.getTrader().getRNG();
        List<Trade> trades = filterTrades(ctx);
        if (trades.isEmpty()) return recipes;
        for (int i = 0; i < (min == max ? min : min + rand.nextInt(max - min)); i++) {
            Trade trade = trades.get(rand.nextInt(recipes.size()));
            recipes.add(trade.generate(ctx));
            if (trade.canDuplicate(ctx)) trades.remove(trade);
        }
        return recipes;
    }
    
    private List<Trade> filterTrades(TradeContext ctx) {
        return trades.stream().filter(trade -> trade.canApply(ctx)).collect(Collectors.toList());
    }
    
    static TradeTable read(JsonElement element) throws Exception {
        if (!element.isJsonObject()) throw new Exception("table must be a JsonObject");
        return new TradeTable(element.getAsJsonObject());
    }


}
