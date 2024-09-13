package net.smileycorp.traders.common.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.traders.common.raid.Raider;

import java.util.List;
import java.util.Random;

public class EntityAILongDistancePatrol extends EntityAIBase {
    
    private final Raider raider;
    private final EntityLiving mob;
    private final double speedModifier = 0.7D;
    private final double leaderSpeedModifier = 0.595D;
    private long cooldownUntil;
    
    public EntityAILongDistancePatrol(Raider raider, EntityLiving mob) {
        this.raider = raider;
        this.mob = mob;
        cooldownUntil = -1;
        setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        return raider.isPatrolling() && mob.getAttackTarget() == null &! mob.isBeingRidden() && mob.world.getWorldTime() >= cooldownUntil;
    }
    
    @Override
    public void updateTask() {
        boolean leader = raider.isPatrolLeader();
        PathNavigate navigation = mob.getNavigator();
        if (navigation.noPath()) {
            List<Entity> list = findPatrolCompanions();
            if (raider.isPatrolling() && list.isEmpty()) return;
            if (leader && raider.getPatrolTarget().distanceSq(mob.getPosition()) < 100) raider.findPatrolTarget();
            Vec3d vec3 = new Vec3d(raider.getPatrolTarget().getX() + 0.5, raider.getPatrolTarget().getY(), raider.getPatrolTarget().getZ() + 0.5);
            Vec3d vec31 = new Vec3d(mob.posX, mob.posY, mob.posZ);
            vec3 = vec31.subtract(vec3).rotateYaw(90).scale(0.4).add(vec3);
            BlockPos blockpos = mob.world.getHeight(new BlockPos(vec3.subtract(vec31).normalize().scale(10).add(vec31)));
            if (!navigation.tryMoveToXYZ(blockpos.getX(), blockpos.getY(),blockpos.getZ(), leader ? leaderSpeedModifier : speedModifier)) {
                moveRandomly();
                cooldownUntil = mob.world.getWorldTime() + 200;
            } else if (leader) for(Entity patroller : list) if (patroller.hasCapability(RaidsContent.RAIDER, null))
                        patroller.getCapability(RaidsContent.RAIDER, null).setPatrolTarget(blockpos);
        }
    }
    
    private List<Entity> findPatrolCompanions() {
        return mob.world.getEntitiesInAABBexcluding(mob, mob.getEntityBoundingBox().grow(16), entity -> {
            if (!entity.hasCapability(RaidsContent.RAIDER, null)) return false;
            return !entity.getCapability(RaidsContent.RAIDER, null).hasActiveRaid();
        });
    }
    
    private boolean moveRandomly() {
        Random rand = mob.getRNG();
        BlockPos blockpos = mob.world.getHeight(mob.getPosition().add(rand.nextInt(16) - 8, 0, rand.nextInt(16) - 8));
        return this.mob.getNavigator().tryMoveToXYZ(blockpos.getX(), blockpos.getY(), blockpos.getZ(), speedModifier);
    }
    
}
