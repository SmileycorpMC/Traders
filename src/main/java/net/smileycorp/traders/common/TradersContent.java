package net.smileycorp.traders.common;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.traders.common.entities.EntityTraderLlama;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class TradersContent {
    
    private static int ID = 170;
    public static final EntityEntry WANDERING_TRADER = EntityEntryBuilder.create().entity(EntityWanderingTrader.class).id(Constants.loc("wandering_trader"), ID++)
            .name(Constants.name("wandering_trader")).egg(4547222, 15377456).tracker(60, 3, true).build();
    public static final EntityEntry TRADER_LLAMA = EntityEntryBuilder.create().entity(EntityTraderLlama.class).id(Constants.loc("trader_llama"), ID++)
            .name(Constants.name("trader_llama")).egg(15377456, 4547222).tracker(60, 3, true).build();
    
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        IForgeRegistry<EntityEntry> registry = event.getRegistry();
        registry.register(WANDERING_TRADER);
        registry.register(TRADER_LLAMA);
    }
    
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        TradersSoundEvents.SOUNDS.forEach(registry::register);
    }
    
}
