package net.smileycorp.traders.common.entities.ai;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class EntityAITraderLookAtTradePlayer extends EntityAIWatchClosest {
    
    private final EntityWanderingTrader trader;
    
    public EntityAITraderLookAtTradePlayer(EntityWanderingTrader trader) {
        super(trader, EntityPlayer.class, 8);
        this.trader = trader;
    }
    
    @Override
    public boolean shouldExecute() {
        if (!trader.isTrading()) return false;
        closestEntity = trader.getCustomer();
        return true;
    }

}
