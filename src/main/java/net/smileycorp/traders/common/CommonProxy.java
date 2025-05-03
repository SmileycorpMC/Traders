package net.smileycorp.traders.common;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.traders.config.CommonConfig;
import net.smileycorp.traders.config.trades.TradeDataLoader;
import net.smileycorp.traders.config.condition.ConditionRegistry;
import net.smileycorp.traders.config.item.functions.FunctionRegistry;
import net.smileycorp.traders.config.values.ValueRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		TradeDataLoader.init(event);
		CommonConfig.syncConfig(event);
	}

	public void init(FMLInitializationEvent event) {
		ConditionRegistry.INSTANCE.registerDefaultConditions();
		ValueRegistry.INSTANCE.registerDefaultValues();
		FunctionRegistry.INSTANCE.registerDefaultValues();
		TradeDataLoader.INSTANCE.loadTrades();
	}
	
	public void postInit(FMLPostInitializationEvent event) {}
	
	public void serverStart(FMLServerStartingEvent event) {

	}
	
	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END | !(event.world instanceof WorldServer)) return;
		WorldServer world = (WorldServer) event.world;
		if (world.provider.getDimension() != 0) return;
		WanderingTraderSpawner.getSpawner(world).tick(world);
	}
	
}
