package net.smileycorp.traders.config.values;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;

public class Item1TypeValue extends ItemTypeValue {
    
    private Item1TypeValue() {}
    
    @Override
    protected ItemStack getItem(TradeContext ctx) {
        return ctx.getItem1();
    }
    
    public static <T extends Comparable<T>> Value<T> deserialize(JsonObject object, DataType<T> type) {
        try {
            if (type != DataType.STRING) throw new ClassCastException();
            return (Value<T>) new Item1TypeValue();
        } catch (Exception e) {
            TradersLogger.logError("invalid value for Item1TypeValue", e);
        }
        return null;
    }
    
}
