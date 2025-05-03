package net.smileycorp.traders.config.item;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.smileycorp.traders.config.item.functions.ItemFunction;
import net.smileycorp.traders.config.trades.TradeContext;
import net.smileycorp.traders.config.values.StaticValue;
import net.smileycorp.traders.config.values.Value;

import java.util.List;

public class ListItem implements TradeItem {
    
    private final List<TradeItem> items;
    private final Value<String> nbt;
    private final Value<Integer> count;
    private final ItemFunction[] functions;
    private final String json;
    
    ListItem(String json, List<TradeItem> items) {
        this(json, items, new StaticValue<>(1), new StaticValue<>("{}"));
    }
    
    ListItem(String json, List<TradeItem> items, Value<Integer> count, Value<String> nbt, ItemFunction... functions) {
        this.items = items;
        this.count = count;
        this.nbt = nbt;
        this.functions = functions;
        this.json = json;
    }
    
    @Override
    public ItemStack get(TradeContext ctx) {
        if (items.size() == 0) return ItemStack.EMPTY;
        ItemStack stack = items.get(ctx.getTrader().getRNG().nextInt(items.size())).get(ctx);
        try {
            stack.setCount(MathHelper.clamp(count.get(ctx), 1, stack.getMaxStackSize()));
            NBTTagCompound nbt = JsonToNBT.getTagFromJson(this.nbt.get(ctx));
            if (nbt != null &! nbt.hasNoTags()) stack.setTagCompound(nbt);
        } catch (Exception e) {}
        for (ItemFunction func :  functions) stack = func.apply(stack, ctx);
        return stack;
    }
    
    @Override
    public String toString() {
        return json;
    }
    
    public static TradeItem deserialize(JsonObject json, Value<Integer> count, Value<String> nbt, ItemFunction... functions) {
        try {
            List<TradeItem> items = Lists.newArrayList();
            for (JsonElement element : json.getAsJsonArray()) {
                TradeItem item = TradeItem.deserialize(element);
                if (item != EMPTY) items.add(item);
            }
            return new ListItem(json.toString(), items, count, nbt, functions);
        } catch (Exception e) {}
        return EMPTY;
    }
    
}
