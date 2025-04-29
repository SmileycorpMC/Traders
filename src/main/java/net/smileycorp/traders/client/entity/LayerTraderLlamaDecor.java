package net.smileycorp.traders.client.entity;

import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.traders.common.Constants;
import net.smileycorp.traders.common.entities.EntityTraderLlama;

public class LayerTraderLlamaDecor implements LayerRenderer<EntityTraderLlama> {
    
    private static final ResourceLocation TEXTURE = Constants.loc("textures/entity/wandering_trader_llama.png");
    private final RenderLlama renderer;
    private final ModelLlama model = new ModelLlama(0.5F);
    
    public LayerTraderLlamaDecor(RenderTraderLLama renderer) {
        this.renderer = renderer;
    }
    
    @Override
    public void doRenderLayer(EntityTraderLlama entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.hasColor()) return;
        renderer.bindTexture(TEXTURE);
        model.setModelAttributes(renderer.getMainModel());
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
}
