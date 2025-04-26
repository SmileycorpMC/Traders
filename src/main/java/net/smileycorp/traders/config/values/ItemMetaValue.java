package net.smileycorp.traders.config.values;

import net.minecraft.item.ItemStack;
import net.smileycorp.traders.config.TradeContext;

public abstract class ItemMetaValue implements Value<Integer> {
    
    @Override
    public Integer get(TradeContext ctx) {
        ItemStack stack = getItem(ctx);
        return stack == null ? 0 : stack.getMetadata();
    }
    
    protected abstract ItemStack getItem(TradeContext ctx);
    
}
