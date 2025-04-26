package net.smileycorp.traders.client.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;

public class ModelWanderingTrader extends ModelVillager {
    
    public ModelWanderingTrader(float scale) {
        super(scale);
        ModelRenderer hat = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
        hat.addBox(-4, -10, -4, 8, 12, 8, scale + 0.45f);
        villagerHead.addChild(hat);
    }
    
}
