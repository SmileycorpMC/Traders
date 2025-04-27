package net.smileycorp.traders.config.item.functions;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.smileycorp.traders.config.TradeContext;

public interface ItemFunction {
    
    ItemStack apply(ItemStack stack, TradeContext ctx);
    
    interface Deserializer {
        
        ItemFunction apply(JsonObject json);
        
    }
    
}
