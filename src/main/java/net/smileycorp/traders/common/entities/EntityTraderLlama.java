package net.smileycorp.traders.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.smileycorp.traders.common.entities.ai.EntityAILlamaDefendTrader;

public class EntityTraderLlama extends EntityLlama {
    
    private int despawnDelay;
    
    public EntityTraderLlama(World world) {
        super(world);
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(1, new EntityAIPanic(this, 2));
        targetTasks.addTask(1, new EntityAILlamaDefendTrader(this));
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (world.isRemote) return;
        if (despawnDelay == 0) return;
        despawnDelay = (getLeashHolder() instanceof EntityWanderingTrader ? ((EntityWanderingTrader) getLeashHolder()).getDespawnDelay()
                : despawnDelay) - 1;
        if (despawnDelay == 0) despawnEntity();
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
