package net.smileycorp.traders.common.entities.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.smileycorp.traders.common.TradersSoundEvents;

public class EntityAITraderDrink extends EntityAIBase {
    
    private final EntityLivingBase entity;
    
    public EntityAITraderDrink(EntityLivingBase entity) {
        this.entity = entity;
    }
    
    @Override
    public boolean shouldExecute() {
        return entity.isInvisible() == entity.world.isDaytime();
    }
    
    @Override
    public void startExecuting() {
        entity.setHeldItem(EnumHand.MAIN_HAND, entity.isInvisible() ? new ItemStack(Items.MILK_BUCKET) :
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.INVISIBILITY));
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return entity.isHandActive();
    }
    
    @Override
    public void resetTask() {
        entity.playSound(entity.isInvisible() ? TradersSoundEvents.WANDERING_TRADER_REAPPEARED :
                        TradersSoundEvents.WANDERING_TRADER_DISAPPEARED, 1, entity.getRNG().nextFloat() * 0.2f + 0.9f);
        entity.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
    }
}
