package net.smileycorp.traders.client.entity;

import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderTraderLLama extends RenderLlama {
    
    public RenderTraderLLama(RenderManager rm) {
        super(rm);
        addLayer(new LayerTraderLlamaDecor(this));
    }
    
}
