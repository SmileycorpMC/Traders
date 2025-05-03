package net.smileycorp.traders.config.trades;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.village.MerchantRecipe;
import net.smileycorp.traders.common.TradersLogger;
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
    
    private TradeTable(int min, int max, List<TradeCondition> conditions, List<Trade> trades) {
        this.min = min;
        this.max = max;
        this.conditions = conditions;
        this.trades = trades;
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
            Trade trade = trades.get(rand.nextInt(trades.size()));
            MerchantRecipe recipe = trade.generate(ctx);
            if (recipe.getItemToBuy().isEmpty()) {
                TradersLogger.logInfo("trade " + trade + " has empty item_1, not adding trade");
                continue;
            }
            if (recipe.getItemToSell().isEmpty()) {
                TradersLogger.logInfo("trade " + trade + " has empty output, not adding trade");
                continue;
            }
            recipes.add(recipe);
            if (!trade.canDuplicate(ctx)) trades.remove(trade);
        }
        return recipes;
    }
    
    private List<Trade> filterTrades(TradeContext ctx) {
        return trades.stream().filter(trade -> trade.canApply(ctx)).collect(Collectors.toList());
    }
    
    static TradeTable deserialize(JsonElement json) throws Exception {
        if (!json.isJsonObject()) throw new Exception("table must be a JsonObject");
        JsonObject obj = json.getAsJsonObject();
        int min = obj.has("min") ? obj.get("min").getAsInt() : 1;
        int max = obj.has("max") ? obj.get("max").getAsInt() : 1;
        if (max < min) throw new IndexOutOfBoundsException("max cannot be less than min");
        List<TradeCondition> conditions = Lists.newArrayList();
        if (obj.has("conditions")) for (JsonElement element : obj.get("conditions").getAsJsonArray()) {
            TradeCondition condition = ConditionRegistry.INSTANCE.readCondition(element.getAsJsonObject());
            if (condition != null) conditions.add(condition);
        }
        List<Trade> trades = Lists.newArrayList();
        if (!obj.has("trades")) throw new NullPointerException("table must specify \"trades\" property");
        for (JsonElement element : obj.get("trades").getAsJsonArray()) {
            try {
                Trade trade = Trade.deserialize(element);
                if (trade != null) {
                    trades.add(trade);
                    TradersLogger.logInfo("Loaded trade " + element);
                }
            } catch (Exception e) {
                TradersLogger.logError("Failed loading trade " + element, e);
            }
        }
        return new TradeTable(min, max, conditions, trades);
    }


}
