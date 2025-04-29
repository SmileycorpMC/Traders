package net.smileycorp.traders.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.traders.client.entity.RenderTraderLLama;
import net.smileycorp.traders.client.entity.RenderWanderingTrader;
import net.smileycorp.traders.common.CommonProxy;
import net.smileycorp.traders.common.Constants;
import net.smileycorp.traders.common.entities.EntityTraderLlama;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

@EventBusSubscriber(value = Side.CLIENT, modid= Constants.MODID)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityWanderingTrader.class, RenderWanderingTrader::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTraderLlama.class, RenderTraderLLama::new);
	}
	
}
