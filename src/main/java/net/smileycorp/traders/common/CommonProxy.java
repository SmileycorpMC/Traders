package net.smileycorp.traders.common;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;
import net.smileycorp.traders.config.EntityConfig;
import net.smileycorp.traders.config.SpawnConfig;
import net.smileycorp.traders.config.condition.ConditionRegistry;
import net.smileycorp.traders.config.item.functions.FunctionRegistry;
import net.smileycorp.traders.config.trades.TradeDataLoader;
import net.smileycorp.traders.config.values.ValueRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		TradeDataLoader.init(event);
		SpawnConfig.syncConfig(event);
		EntityConfig.syncConfig(event);
	}

	public void init(FMLInitializationEvent event) {
		ConditionRegistry.INSTANCE.registerDefaultConditions();
		ValueRegistry.INSTANCE.registerDefaultValues();
		FunctionRegistry.INSTANCE.registerDefaultValues();
		TradeDataLoader.INSTANCE.loadTrades();
	}
	
	public void postInit(FMLPostInitializationEvent event) {}
	
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSpawnTrader());
	}
	
	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {
		if (!SpawnConfig.tradersSpawn || event.phase != TickEvent.Phase.END | !(event.world instanceof WorldServer)) return;
		WorldServer world = (WorldServer) event.world;
		if (world.provider.getDimension() != 0) return;
		WanderingTraderSpawner.getSpawner(world).tick(world);
	}

	@SubscribeEvent
	public static void entityAdded(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote) return;
		if (!EntityConfig.attacksTraders(event.getEntity())) return;
		EntityLiving entity = (EntityLiving) event.getEntity();
		entity.targetTasks.addTask(1, entity instanceof EntityCreature ?
				new EntityAINearestAttackableTarget<>((EntityCreature) entity, EntityWanderingTrader.class, false)
				: new EntityAIFindEntityNearest(entity, EntityWanderingTrader.class));
	}
	
}
