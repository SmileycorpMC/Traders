package net.smileycorp.traders.common.entities.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class EntityAITraderWander extends EntityAIBase {
    
    private final EntityWanderingTrader trader;
    private final double speedModifier = 0.35D;
    
    public EntityAITraderWander(EntityWanderingTrader trader) {
        this.trader = trader;
        setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        BlockPos target = trader.getWanderTarget();
        return target != null && target.distanceSqToCenter(trader.posX, trader.posY, trader.posZ) >= 4;
    }
    
    @Override
    public void updateTask() {
        BlockPos target = trader.getWanderTarget();
        if (target == null |! trader.getNavigator().noPath()) return;
        if (target.distanceSqToCenter(trader.posX, trader.posY, trader.posZ) < 100) {
            trader.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), speedModifier);
            return;
        }
        Vec3d dir = DirectionUtils.getDirectionVec(new Vec3d(trader.posX, trader.posY, trader.posZ), new Vec3d(target)).normalize();
        trader.getNavigator().tryMoveToXYZ(dir.x * 10d + trader.posX, dir.y * 10d + trader.posY, dir.z * 10d + trader.posZ, speedModifier);
    }
    
    @Override
    public void resetTask() {
        trader.setWanderTarget(null);
        trader.getNavigator().clearPath();
    }
    
}
