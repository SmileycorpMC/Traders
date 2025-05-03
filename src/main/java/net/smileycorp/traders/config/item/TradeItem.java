package net.smileycorp.traders.config.item;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.config.trades.TradeContext;
import net.smileycorp.traders.config.item.functions.FunctionRegistry;
import net.smileycorp.traders.config.item.functions.ItemFunction;
import net.smileycorp.traders.config.values.StaticValue;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

import java.util.List;

public interface TradeItem {
    
    TradeItem EMPTY = ctx -> ItemStack.EMPTY;
    
    ItemStack get(TradeContext ctx);
    
    static TradeItem deserialize(JsonElement json) {
        if (json.isJsonArray()) {
            List<TradeItem> items = Lists.newArrayList();
            for (JsonElement element : json.getAsJsonArray()) {
                TradeItem item = deserialize(element);
                if (item != EMPTY) items.add(item);
                return new ListItem(json.toString(), items);
            }
        }
        if (!json.isJsonObject()) return EMPTY;
        JsonObject obj = json.getAsJsonObject();
        Deserializer deserializer = null;
        if (obj.has("item")) deserializer = StandardItem::deserialize;
        else if (obj.has("items")) deserializer = ListItem::deserialize;
        else if (obj.has("ore")) deserializer = OreDictItem::deserialize;
        if (deserializer == null) return EMPTY;
        Value<Integer> count = obj.has("count") ? ValueRegistry.INSTANCE.readValue(DataType.INT, obj.get("count")) : new StaticValue<>(1);
        Value<String> nbt = obj.has("nbt") ? ValueRegistry.INSTANCE.readValue(DataType.STRING, obj.get("nbt")) : new StaticValue<>("{}");
        List<ItemFunction> functions = Lists.newArrayList();
        if (obj.has("functions")) for (JsonElement element : obj.get("functions").getAsJsonArray()) {
            ItemFunction func = FunctionRegistry.INSTANCE.readFunction(element.getAsJsonObject());
            if (func != null) functions.add(func);
        }
        return deserializer.apply(obj, count, nbt, functions.toArray(new ItemFunction[functions.size()]));
    }
    
    interface Deserializer {
        
        TradeItem apply(JsonObject json, Value<Integer> count, Value<String> nbt, ItemFunction... functions);
        
    }
    
}
