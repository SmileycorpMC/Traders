package net.smileycorp.traders.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class LayerHeldItemTrader implements LayerRenderer<EntityWanderingTrader> {
    
    private final RenderWanderingTrader renderer;
    
    public LayerHeldItemTrader(RenderWanderingTrader renderer) {
        this.renderer = renderer;
    }
    
    public void doRenderLayer(EntityWanderingTrader entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack stack = entity.getHeldItemMainhand();
        if (stack.isEmpty()) return;
        GlStateManager.color(1, 1, 1);
        GlStateManager.pushMatrix();
        renderer.getMainModel().villagerNose.postRender(0.0625F);
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.translate(0, 0.4f, -0.4f);
        GlStateManager.rotate(180, 1, 0,  0);
        mc.getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        GlStateManager.popMatrix();
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
}
