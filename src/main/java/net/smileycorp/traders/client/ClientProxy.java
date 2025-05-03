package net.smileycorp.traders.client;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
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
import net.smileycorp.traders.config.ClientConfig;

@EventBusSubscriber(value = Side.CLIENT, modid= Constants.MODID)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ClientConfig.syncConfig(event);
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
	
	@SubscribeEvent
	public static void playSound(PlaySoundEvent event) {
		ISound sound = event.getSound();
		if (sound == null) return;
		if (!Constants.MODID.equals(sound.getSoundLocation().getResourceDomain()))
		if (ClientConfig.silentTraders) {
			event.setResultSound(null);
			return;
		}
		if (!(sound instanceof PositionedSound)) return;
		((PositionedSound)sound).volume = sound.getVolume() * ClientConfig.traderVolume;
		event.setResultSound(sound);
	}
	
}
