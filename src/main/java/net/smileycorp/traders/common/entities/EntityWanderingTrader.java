package net.smileycorp.traders.common.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.traders.common.TradersSoundEvents;
import net.smileycorp.traders.common.entities.ai.EntityAITraderDrink;
import net.smileycorp.traders.common.entities.ai.EntityAITraderLookAtTradePlayer;
import net.smileycorp.traders.common.entities.ai.EntityAITraderTradePlayer;
import net.smileycorp.traders.common.entities.ai.EntityAITraderWander;
import net.smileycorp.traders.config.TradeDataLoader;
import net.smileycorp.traders.integration.RaidsIntegration;

import javax.annotation.Nullable;

public class EntityWanderingTrader extends EntityAgeable implements INpc, IMerchant{
    
    private EntityPlayer buyingPlayer;
    private MerchantRecipeList offers;
    private BlockPos wanderTarget;
    private int despawnDelay;
    
    public EntityWanderingTrader(World world) {
        super(world);
        this.setSize(0.6f, 1.95f);
    }
    
    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(0, new EntityAITraderDrink(this));
        tasks.addTask(1, new EntityAITraderTradePlayer(this));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8, 0.5, 0.5));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12, 0.5, 0.5));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8, 0.5, 0.5));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8, 0.5, 0.5));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityIllusionIllager.class, 12, 0.5, 0.5));
        if (Loader.isModLoaded("raids")) RaidsIntegration.addTasks(this);
        tasks.addTask(1, new EntityAIPanic(this, 0.5));
        tasks.addTask(1, new EntityAITraderLookAtTradePlayer(this));
        tasks.addTask(2, new EntityAITraderWander(this));
        tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.35));
        tasks.addTask(8, new EntityAIWanderAvoidWater(this, 0.6));
        tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3, 1));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data) {
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus",
                rand.nextGaussian() * 0.05, 1));
        setLeftHanded(rand.nextFloat() < 0.05f);
        populateBuyingList();
        return data;
    }
    
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == Items.NAME_TAG) {
            stack.interactWithEntity(player, this, hand);
            return true;
        }
        if (holdingSpawnEggOfClass(stack, this.getClass()) |! isEntityAlive() || isTrading() || player.isSneaking()) return super.processInteract(player, hand);
        if (world.isRemote) return true;
        if (offers == null) populateBuyingList();
        if (hand == EnumHand.MAIN_HAND) player.addStat(StatList.TALKED_TO_VILLAGER);
        if (offers.isEmpty()) return super.processInteract(player, hand);
        setCustomer(player);
        player.displayVillagerTradeGui(this);
        return true;
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (world.isRemote) return;
        if (despawnDelay == 0 || isTrading()) return;
        if (despawnDelay-- == 0) despawnEntity();
    }
    
    public boolean isTrading() {
        return buyingPlayer != null;
    }
    
    private void populateBuyingList() {
        offers = TradeDataLoader.INSTANCE.getTrades(this);
    }
    
    @Override
    protected void updateItemUse(ItemStack stack, int eatingParticleCount) {
        if (stack.isEmpty() |! isHandActive()) return;
        EnumAction action = stack.getItemUseAction();
        if (action == EnumAction.EAT) super.updateItemUse(stack, eatingParticleCount);
        else if (action == EnumAction.DRINK) playSound(stack.getItem() == Items.MILK_BUCKET ? TradersSoundEvents.WANDERING_TRADER_DRINK_MILK :
                        TradersSoundEvents.WANDERING_TRADER_DRINK_POTION, 0.5f, world.rand.nextFloat() * 0.1f + 0.9f);
    }
    
    public void setDespawnDelay(int delay) {
        despawnDelay = delay;
    }
    
    public int getDespawnDelay() {
        return despawnDelay;
    }
    
    public void setWanderTarget(BlockPos pos) {
        wanderTarget = pos;
    }
    
    public BlockPos getWanderTarget() {
        return wanderTarget;
    }
    
    @Override
    public void setCustomer(EntityPlayer player) {
        buyingPlayer = player;
    }
    
    @Override
    public EntityPlayer getCustomer() {
        return buyingPlayer;
    }
    
    @Override
    public MerchantRecipeList getRecipes(EntityPlayer entityPlayer) {
        return offers;
    }
    
    @Override
    public void setRecipes(MerchantRecipeList trades) {
        offers = trades;
    }
    
    @Override
    public void useRecipe(MerchantRecipe recipe) {
        recipe.compensateToolUses();
        livingSoundTime = -getTalkInterval();
        playSound(TradersSoundEvents.WANDERING_TRADER_YES, getSoundVolume(), getSoundPitch());
        if (!recipe.getRewardsExp()) return;
        world.spawnEntity(new EntityXPOrb(world, posX, posY + 0.5, posZ, 3 + rand.nextInt(4)));
    }
    
    @Override
    public void verifySellingItem(ItemStack stack) {
        if (world.isRemote || livingSoundTime <= 20 -getTalkInterval()) return;
        livingSoundTime = -getTalkInterval();
        playSound(stack.isEmpty() ? TradersSoundEvents.WANDERING_TRADER_NO : TradersSoundEvents.WANDERING_TRADER_YES,
                getSoundVolume(), getSoundPitch());
    }
    
    @Override
    public World getWorld() {
        return world;
    }
    
    @Override
    public BlockPos getPos() {
        return getPosition();
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }
    
    @Override
    public int getGrowingAge() {
        return 0;
    }
    
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return isTrading() ? TradersSoundEvents.WANDERING_TRADER_TRADE : TradersSoundEvents.WANDERING_TRADER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TradersSoundEvents.WANDERING_TRADER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return TradersSoundEvents.WANDERING_TRADER_DEATH;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VILLAGER;
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("DespawnDelay")) despawnDelay = nbt.getInteger("DespawnDelay");
        if (nbt.hasKey("WanderTarget")) wanderTarget = NBTUtil.getPosFromTag(nbt.getCompoundTag("WanderTarget"));
        if (nbt.hasKey("Offers")) offers = new MerchantRecipeList(nbt.getCompoundTag("Offers"));
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("DespawnDelay", despawnDelay);
        nbt.setTag("WanderTarget", NBTUtil.createPosTag(wanderTarget));
        nbt.setTag("Offers", offers.getRecipiesAsTags());
    }
    
}
