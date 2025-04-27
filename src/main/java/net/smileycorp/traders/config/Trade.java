package net.smileycorp.traders.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.smileycorp.traders.config.condition.TradeCondition;
import net.smileycorp.traders.config.item.TradeItem;
import net.smileycorp.traders.config.values.Value;

public class Trade {
    
    private final TradeItem item1, item2, output;
    private final Value<Integer> max_uses;
    private final Value<Boolean> can_duplicate;
    
   private Trade(TradeItem item1, TradeItem item2, TradeItem output, Value<Integer> max_uses, Value<Boolean> can_duplicate, TradeCondition... conditions) {
        this.conditions = conditions;
        this.item1 = item1;
        this.item2 = item2;
        this.output = output;
        this.max_uses = max_uses;
        this.can_duplicate = can_duplicate;
    }
    
    private final TradeCondition[] conditions;
    
    public boolean canApply(TradeContext ctx) {
        for (TradeCondition condition : conditions) if (!condition.apply(ctx)) return false;
        return true;
    }
    
    public boolean canDuplicate(TradeContext ctx) {
        return can_duplicate.get(ctx);
    }
    
    public MerchantRecipe generate(TradeContext ctx) {
        ItemStack item1 = this.item1.get(ctx);
        ctx = ctx.copy().item1(item1);
        ItemStack item2 = this.item2.get(ctx);
        ctx.item2(item2);
        int max_uses = this.max_uses.get(ctx);
        return new MerchantRecipe(item1, item2, output.get(ctx), 0, max_uses);
    }
    
    public static Trade deserialize(JsonElement json) {
        if (json.isJsonObject()) return null;
        
    }
    
}
