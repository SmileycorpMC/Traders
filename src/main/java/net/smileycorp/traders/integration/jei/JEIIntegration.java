package net.smileycorp.traders.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.smileycorp.traders.config.trades.Trade;
import net.smileycorp.traders.config.trades.TradeDataLoader;
import net.smileycorp.traders.config.trades.TradeTable;

@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = helpers.getGuiHelper();
        registry.addRecipeCategories(new TraderRecipeCategory(guiHelper));
    }

    @Override
    public void register(IModRegistry registry) {
        for (TradeTable table : TradeDataLoader.INSTANCE.getTrades()) {
            if (!table.addToJEI()) continue;
            for (Trade trade : table) {
                if (!trade.addToJEI()) continue;

            }
        }
    }

}
