package net.smileycorp.traders.config.trades;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.config.condition.ConditionRegistry;
import net.smileycorp.traders.config.condition.TradeCondition;
import net.smileycorp.traders.config.item.TradeItem;
import net.smileycorp.traders.config.values.StaticValue;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

import java.util.List;

public class Trade {
    
    private final List<TradeCondition> conditions;
    private final TradeItem item1, item2, output;
    private final Value<Integer> max_uses;
    private final Value<Boolean> can_duplicate;
    
   private Trade(TradeItem item1, TradeItem item2, TradeItem output, Value<Integer> max_uses, Value<Boolean> can_duplicate, List<TradeCondition> conditions) {
        this.item1 = item1;
        this.item2 = item2;
        this.output = output;
        this.max_uses = max_uses;
        this.can_duplicate = can_duplicate;
        this.conditions = conditions;
    }
    
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
    
    @Override
    public String toString() {
       StringBuilder builder = new StringBuilder(super.toString());
       builder.append("[");
       if (item1 != TradeItem.EMPTY) builder.append(item1);
       if (item2 != TradeItem.EMPTY) builder.append(" + " + item2);
       if (output != TradeItem.EMPTY) builder.append(" -> " + output);
       builder.append("]");
       return builder.toString();
    }
    
    public static Trade deserialize(JsonElement json) throws Exception {
        if (!json.isJsonObject()) throw new Exception("Trade must be a Json Object");
        JsonObject obj = json.getAsJsonObject();
        if (!obj.has("item_1")) throw new Exception("Trade must specify item_1");
        if (!obj.has("output")) throw new Exception("Trade must specify output");
        TradeItem item2 = obj.has("item_2") ? TradeItem.deserialize(obj.get("item_2")) : TradeItem.EMPTY;
        Value<Integer> maxUses = obj.has("max_uses") ? ValueRegistry.INSTANCE.readValue(DataType.INT, obj.get("max_uses")) :
                new StaticValue<>(1);
        Value<Boolean> canDuplicate = obj.has("can_duplicate") ? ValueRegistry.INSTANCE.readValue(DataType.BOOLEAN, obj.get("can_duplicate")) :
                new StaticValue<>(false);
        List<TradeCondition> conditions = Lists.newArrayList();
        if (obj.has("conditions")) for (JsonElement element : obj.get("conditions").getAsJsonArray()) {
            TradeCondition condition = ConditionRegistry.INSTANCE.readCondition(element.getAsJsonObject());
            if (condition != null) conditions.add(condition);
        }
        return new Trade(TradeItem.deserialize(obj.get("item_1")), item2, TradeItem.deserialize(obj.get("output")), maxUses, canDuplicate, conditions);
    }
    
}
