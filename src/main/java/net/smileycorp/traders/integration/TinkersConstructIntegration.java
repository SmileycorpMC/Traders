package net.smileycorp.traders.integration;

import net.minecraft.entity.monster.EntityVindicator;
import net.minecraftforge.fluids.FluidStack;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerFluids;

public class TinkersConstructIntegration {

    //yeah, you can melt this fuck now
    //enjoy your free emeralds sickos
    public static void registerRecipes() {
        TinkerRegistry.registerEntityMelting(EntityWanderingTrader.class, new FluidStack(TinkerFluids.emerald, 6));
    }

}
