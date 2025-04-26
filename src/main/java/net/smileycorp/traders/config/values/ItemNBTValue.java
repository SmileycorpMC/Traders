package net.smileycorp.traders.config.values;

import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.config.TradeContext;

public abstract class ItemNBTValue<T extends Comparable<T>> extends NBTValue<T> {
    
    public ItemNBTValue(Value<String> value, DataType<T> type) {
        super(value, type);
    }
    
    protected abstract ItemStack getItem(TradeContext ctx);
    
}
