package net.smileycorp.traders.common.entities.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class EntityAITraderTradePlayer extends EntityAIBase {
    
    private final EntityWanderingTrader trader;
    
    public EntityAITraderTradePlayer(EntityWanderingTrader trader) {
        this.trader = trader;
        setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!trader.isEntityAlive() || trader.isInWater() |! trader.onGround || trader.velocityChanged) return false;
        EntityPlayer customer = trader.getCustomer();
        if (customer == null) return false;
        return trader.getDistanceSq(customer) <= 16 && customer.openContainer != null;
    }
    
    @Override
    public void startExecuting() {
        trader.getNavigator().clearPath();
    }
    
    @Override
    public void resetTask() {
        trader.setCustomer(null);
    }
    
}
