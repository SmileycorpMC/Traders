package net.smileycorp.traders.config.values;

import net.minecraft.item.ItemStack;
import net.smileycorp.traders.config.TradeContext;

public abstract class ItemTypeValue implements Value<String> {
    
    @Override
    public String get(TradeContext ctx) {
        ItemStack stack = getItem(ctx);
        return stack == null ? "minecraft:air" : stack.getItem().getRegistryName().toString();
    }
    
    protected abstract ItemStack getItem(TradeContext ctx);
    
}
