package net.smileycorp.traders.common.entities.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.smileycorp.traders.common.entities.EntityTraderLlama;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class EntityAILlamaDefendTrader extends EntityAITarget {
    
    private final EntityTraderLlama llama;
    private int lastHurt;
    
    public EntityAILlamaDefendTrader(EntityTraderLlama llama) {
        super(llama, false);
        this.llama = llama;
    }
    
    @Override
    public boolean shouldExecute() {
        if (!(llama.getLeashHolder() instanceof EntityWanderingTrader)) return false;
        EntityWanderingTrader trader = (EntityWanderingTrader) llama.getLeashHolder();
        EntityLivingBase lastHurtBy = trader.getLastAttackedEntity();
        if (lastHurtBy == null) return false;
        int lastHurt = trader.getLastAttackedEntityTime();
        if (lastHurt == this.lastHurt) return true;
        if (!llama.canAttackClass(lastHurtBy.getClass())) return false;
        llama.setAttackTarget(lastHurtBy);
        this.lastHurt = lastHurt;
        return true;
    }
    
}
