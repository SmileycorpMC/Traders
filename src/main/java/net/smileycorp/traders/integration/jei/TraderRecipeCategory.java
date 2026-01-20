package net.smileycorp.traders.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.traders.common.Constants;

import java.util.List;

public class TraderRecipeCategory implements IRecipeCategory<TraderRecipeCategory.Wrapper> {

    public static final String ID = Constants.locStr("trading");
    public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/jei_background.png");

    private final IDrawable background, icon;

    public TraderRecipeCategory (IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 106, 98);
        icon = guiHelper.createDrawable(TEXTURE, 106, 0, 16, 16);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        items.init(0, true, 1, 76);
        items.init(1, true, 26, 76);
        items.init(2, false, 85, 76);
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        items.set(1, inputs.get(0));
        items.set(1, inputs.get(0));
        items.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public String getUid() {
        return ID;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.category.traders.trading");
    }

    @Override
    public String getModName() {
        return Constants.MODID;
    }

    public static class Wrapper implements IRecipeWrapper {

        @Override
        public void getIngredients(IIngredients ingredient) {
        }

    }

}
