package net.smileycorp.traders.config.trades;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class TradeContext {

    public static final TradeContext DEFAULT = new TradeContext(new EntityWanderingTrader(null));

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
        WorldServer world = (WorldServer) trader.getWorld();
        return world == null ? FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0) : world;
    }

    public boolean ignoreConditions() {
        return trader.world == null;
    }
    
    public TradeContext copy() {
        return new TradeContext(trader).item1(item1).item2(item2);
    }
    
}
