package net.smileycorp.traders.integration;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.smileycorp.raids.common.entities.EntityPillager;
import net.smileycorp.raids.common.entities.EntityRavager;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

public class RaidsIntegration {
    
    public static void addTasks(EntityWanderingTrader entity) {
        entity.tasks.addTask(1, new EntityAIAvoidEntity(entity, EntityPillager.class, 15, 0.5, 0.5));
        entity.tasks.addTask(1, new EntityAIAvoidEntity(entity, EntityRavager.class, 10, 0.5, 0.5));
    }
    
}
