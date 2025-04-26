package net.smileycorp.traders.config.condition;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

public class LocalDifficultyCondition implements TradeCondition {

	protected Value<Double> difficulty;

	public LocalDifficultyCondition(Value<Double> difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public boolean apply(TradeContext ctx) {
		return ctx.getWorld().getDifficultyForLocation(ctx.getTrader().getPos()).getClampedAdditionalDifficulty() > difficulty.get(ctx);
	}

	public static LocalDifficultyCondition deserialize(JsonObject json) {
		try {
			return new LocalDifficultyCondition(ValueRegistry.INSTANCE.readValue(DataType.DOUBLE, json.get("value")));
		} catch(Exception e) {
			TradersLogger.logError("Incorrect parameters for LocalDifficultyCondition", e);
		}
		return null;
	}

}
