package net.smileycorp.traders.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.smileycorp.traders.common.entities.ai.EntityAILlamaDefendTrader;
import net.smileycorp.traders.config.EntityConfig;

public class EntityTraderLlama extends EntityLlama {
    
    private int despawnDelay = -1;
    
    public EntityTraderLlama(World world) {
        super(world);
    }
    
    @Override
    protected void initEntityAI() {
        tasks.addTask(1, new EntityAIPanic(this, 2));
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
        tasks.addTask(2, new EntityAILlamaFollowCaravan(this, 2.0999999046325684));
        tasks.addTask(3, new EntityAIAttackRanged(this, 1.25, 40, 20.0F));
        tasks.addTask(3, new EntityAIPanic(this, 1.2));
        tasks.addTask(4, new EntityAIMate(this, 1.0));
        tasks.addTask(5, new EntityAIFollowParent(this, 1.0));
        tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new AIHurtByTarget(this));
        targetTasks.addTask(1, new EntityAILlamaDefendTrader(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        EntityConfig.traderLlama.applyAttributes(this);
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (world.isRemote) return;
        if (despawnDelay < 0) return;
        despawnDelay = (getLeashHolder() instanceof EntityWanderingTrader ? ((EntityWanderingTrader) getLeashHolder()).getDespawnDelay()
                : despawnDelay) - 1;
        if (despawnDelay == 0) setDead();
    }
    
    public void setDespawnDelay(int delay) {
        despawnDelay = delay;
    }
    
    public int getDespawnDelay() {
        return despawnDelay;
    }
    
    @Override
    protected void mountTo(EntityPlayer player) {
        Entity holder = getLeashHolder();
        if (!(holder instanceof EntityWanderingTrader)) super.mountTo(player);
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("DespawnDelay")) despawnDelay = nbt.getInteger("DespawnDelay");
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("DespawnDelay", despawnDelay);
    }
    
}
