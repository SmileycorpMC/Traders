package net.smileycorp.traders.common;

import com.google.common.collect.Sets;
import net.minecraft.util.SoundEvent;

import java.util.Set;

public class TradersSoundEvents {
    
    public static final Set<SoundEvent> SOUNDS = Sets.newHashSet();
    
    public static final SoundEvent WANDERING_TRADER_AMBIENT = register("entity.wandering_trader.ambient");
    public static final SoundEvent WANDERING_TRADER_DEATH = register("entity.wandering_trader.death");
    public static final SoundEvent WANDERING_TRADER_DISAPPEARED = register("entity.wandering_trader.disappeared");
    public static final SoundEvent WANDERING_TRADER_DRINK_MILK = register("entity.wandering_trader.drink_milk");
    public static final SoundEvent WANDERING_TRADER_DRINK_POTION = register("entity.wandering_trader.drink_potion");
    public static final SoundEvent WANDERING_TRADER_HURT = register("entity.wandering_trader.hurt");
    public static final SoundEvent WANDERING_TRADER_NO = register("entity.wandering_trader.no");
    public static final SoundEvent WANDERING_TRADER_REAPPEARED = register("entity.wandering_trader.reappeared");
    public static final SoundEvent WANDERING_TRADER_TRADE = register("entity.wandering_trader.trade");
    public static final SoundEvent WANDERING_TRADER_YES = register("entity.wandering_trader.yes");

    public static SoundEvent register(String name) {
        SoundEvent sound = new SoundEvent(Constants.loc(name));
        sound.setRegistryName(name);
        SOUNDS.add(sound);
        return sound;
    }

}
