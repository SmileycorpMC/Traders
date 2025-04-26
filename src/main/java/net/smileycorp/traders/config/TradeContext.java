package net.smileycorp.traders.config;

import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class TradeContext {
    
    private final EntityWanderingTrader trader;
    private ItemStack item1 = ItemStack.EMPTY, item2 = ItemStack.EMPTY;
    
    public TradeContext(EntityWanderingTrader trader) {
        this.trader = trader;
    }
    
    public TradeContext item1(ItemStack stack) {
        item1 = stack;
        return this;
    }
    
    public TradeContext item2(ItemStack stack) {
        item1 = stack;
        return this;
    }
    
    public EntityWanderingTrader getTrader() {
        return trader;
    }
    
    public ItemStack getItem1() {
        return item1;
    }
    
    public ItemStack getItem2() {
        return item2;
    }
    
    public WorldServer getWorld() {
        return (WorldServer) trader.getWorld();
    }
    
    public TradeContext copy() {
        return new TradeContext(trader).item1(item1).item2(item2);
    }
    
}
