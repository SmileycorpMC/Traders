package net.smileycorp.traders.config.values;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;

public class Item2TypeValue extends ItemTypeValue {
    
    private Item2TypeValue() {}
    
    @Override
    protected ItemStack getItem(TradeContext ctx) {
        return ctx.getItem2();
    }
    
    public static <T extends Comparable<T>> Value<T> deserialize(JsonObject object, DataType<T> type) {
        try {
            if (type != DataType.STRING) throw new ClassCastException();
            return (Value<T>) new Item2TypeValue();
        } catch (Exception e) {
            TradersLogger.logError("invalid value for Item2TypeValue", e);
        }
        return null;
    }
    
}
