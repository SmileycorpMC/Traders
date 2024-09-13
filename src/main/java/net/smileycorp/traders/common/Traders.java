package net.smileycorp.traders.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class Traders {

	@SidedProxy(clientSide = Constants.CLIENT, serverSide = Constants.SERVER)
	public static CommonProxy proxy;

	public static final EntityEntry WANDERING_TRADER = EntityEntryBuilder.create().entity(EntityWanderingTrader.class).id(Constants.loc("wandering_trader"), 49)
			.name(Constants.name("Wandering_Trader")).egg(4547222, 15377456)
			.tracker(64, 3, true).build();
	
	public Traders() {
		TradersLogger.clearLog();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event){
		proxy.serverStart(event);
	}
	
}
