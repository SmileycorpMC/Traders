package net.smileycorp.traders.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.smileycorp.traders.config.TradeDataLoader;
import net.smileycorp.traders.config.condition.ConditionRegistry;
import net.smileycorp.traders.config.item.functions.FunctionRegistry;
import net.smileycorp.traders.config.values.ValueRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		TradeDataLoader.init(event);
	}

	public void init(FMLInitializationEvent event) {
		ConditionRegistry.INSTANCE.registerDefaultConditions();
		ValueRegistry.INSTANCE.registerDefaultValues();
		FunctionRegistry.INSTANCE.registerDefaultValues();
		TradeDataLoader.INSTANCE.loadTrades();
	}
	
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public void serverStart(FMLServerStartingEvent event) {

	}
	
}
