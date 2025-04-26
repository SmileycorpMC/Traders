package net.smileycorp.traders.config.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.EnumDifficulty;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

public class GameDifficultyCondition implements TradeCondition {
    
    protected Value<?> difficulty;
    
    public GameDifficultyCondition(Value<?> difficulty) {
        this.difficulty = difficulty;
    }
    
    @Override
    public boolean apply(TradeContext ctx) {
        Comparable value = difficulty.get(ctx);
        return ctx.getWorld().getDifficulty() == (value instanceof String ? EnumDifficulty.valueOf((String) value) : EnumDifficulty.getDifficultyEnum((Integer) value));
    }
    
    public static GameDifficultyCondition deserialize(JsonObject json) {
        try {
            Value getter;
            try {
                getter = ValueRegistry.INSTANCE.readValue(DataType.STRING, json.get("value"));
            } catch (Exception e) {
                getter = ValueRegistry.INSTANCE.readValue(DataType.INT, json);
            }
            return new GameDifficultyCondition(getter);
        } catch(Exception e) {
            TradersLogger.logError("Incorrect parameters for GameDifficultyCondition", e);
        }
        return null;
    }
    
}
