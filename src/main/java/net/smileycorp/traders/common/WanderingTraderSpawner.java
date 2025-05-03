package net.smileycorp.traders.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.smileycorp.traders.common.entities.EntityTraderLlama;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;
import net.smileycorp.traders.config.CommonConfig;

public class WanderingTraderSpawner {
    
    private final WorldData data;
    private int tickDelay;
    
    public WanderingTraderSpawner(WorldData data) {
        this.data = data;
        tickDelay = CommonConfig.tickDelay;
    }
    
    public void tick(WorldServer world) {
        if (tickDelay-- > 0) return;
        tickDelay = CommonConfig.tickDelay;
        data.spawnDelay -= 1200;
        if (data.spawnDelay > 0) return;
        data.spawnDelay = CommonConfig.spawnDelay;
        if (!world.getGameRules().getBoolean("doMobSpawning")) return;
        if (world.rand.nextInt(100) > data.spawnChance) if (spawn(world)) {
            data.spawnChance = CommonConfig.spawnChance;
            return;
        }
        data.spawnChance = MathHelper.clamp(data.spawnChance + CommonConfig.spawnChanceIncrement, CommonConfig.spawnChance, CommonConfig.maxSpawnChance);
    }
    
    private boolean spawn(WorldServer world) {
        if (world.playerEntities.isEmpty()) return false;
        return spawn(world, world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()))) != null;
    }
    
    public EntityWanderingTrader spawn(WorldServer world, EntityPlayer player) {
        BlockPos pos = player.getPosition();
        Village village = world.getVillageCollection().getNearestVillage(pos, 48);
        if (village != null) pos = village.getCenter();
        BlockPos start = findSpawnPosition(world, pos, 48);
        if (world.collidesWithAnyBlock(new AxisAlignedBB(pos, pos.add(1, 2, 1)))) return null;
        EntityWanderingTrader trader = new EntityWanderingTrader(world);
        trader.setPosition(start.getX() + 0.5f, start.getY(), start.getZ() + 0.5f);
        if (!world.spawnEntity(trader)) return null;
        for (int i = 0; i < 2; i++) spawnLlama(world, trader);
        trader.setHomePosAndDistance(pos, 16);
        trader.setWanderTarget(pos);
        trader.setDespawnDelay(48000);
        TradersLogger.logInfo("Spawned trader at " + start);
        return trader;
    }
    
    private void spawnLlama(WorldServer world, EntityWanderingTrader trader) {
        BlockPos pos = findSpawnPosition(world, trader.getPosition(), 4);
        EntityTraderLlama llama = new EntityTraderLlama(world);
        llama.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
        world.spawnEntity(llama);
        llama.setLeashHolder(trader, true);
    }
    
    private BlockPos findSpawnPosition(WorldServer world, BlockPos pos, int range) {
        for (int i = 0; i < 10; i++) {
            int x = pos.getX() + world.rand.nextInt(range * 2) - range;
            int z = pos.getZ() + world.rand.nextInt(range * 2) - range;
            BlockPos pos1 = new BlockPos(x, world.getHeight(x, z), z);
            if (world.getBlockState(pos.down()).isNormalCube()) return pos1;
        }
        return pos;
    }
    
    public static WanderingTraderSpawner getSpawner(WorldServer world) {
        return WorldData.getData(world).getSpawner();
    }
    
    public static class WorldData extends WorldSavedData {
        
        public static final String DATA = "wandering_traders";
        private WanderingTraderSpawner spawner;
        private int spawnDelay, spawnChance;
        
        public WorldData() {
            this(DATA);
        }
        
        public WorldData(String data) {
            super(data);
            spawnDelay = CommonConfig.spawnDelay;
            spawnChance = CommonConfig.spawnChance;
        }
        
        public WanderingTraderSpawner getSpawner() {
            if (spawner == null) spawner = new WanderingTraderSpawner(this);
            return spawner;
        }
        
        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            spawnDelay = nbt.getInteger("spawnDelay");
            spawnChance = nbt.getInteger("spawnChance");
        }
        
        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            compound.setInteger("spawnDelay", spawnDelay);
            compound.setInteger("spawnChance", spawnChance);
            return compound;
        }
        
        public static WorldData getData(WorldServer world) {
            WorldData data = (WorldData) world.getMapStorage().getOrLoadData(WorldData.class, DATA);
            if (data == null) {
                data = new WorldData();
                world.getMapStorage().setData(DATA, data);
            }
            return data;
        }
        
    }
}
