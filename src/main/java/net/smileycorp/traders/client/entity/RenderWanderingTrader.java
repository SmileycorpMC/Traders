package net.smileycorp.traders.client.entity;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.traders.common.Constants;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class RenderWanderingTrader extends RenderLiving<EntityWanderingTrader> {
    
    private static final ResourceLocation TEXTURE = Constants.loc("textures/entity/wandering_trader.png");

    public RenderWanderingTrader(RenderManager rm) {
        super(rm, new ModelWanderingTrader(0), 0.5f);
        addLayer(new LayerHeldItemTrader(this));
    }
    
    @Override
    public ModelWanderingTrader getMainModel() {
        return (ModelWanderingTrader) super.getMainModel();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityWanderingTrader entity) {
        return TEXTURE;
    }

    @Override
	protected void preRenderCallback(EntityWanderingTrader entity, float partialTicks) {
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
}
