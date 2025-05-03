package net.smileycorp.traders.config.condition;

import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.trades.TradeContext;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

public class RandomCondition implements TradeCondition {

	protected Value<Double> chance;

	public RandomCondition(Value<Double> chance) {
		this.chance = chance;
	}

	@Override
	public boolean apply(TradeContext ctx) {
		return ctx.getTrader().getRNG().nextFloat() <= chance.get(ctx);
	}

	public static RandomCondition deserialize(JsonObject json) {
		try {
			return new RandomCondition(ValueRegistry.INSTANCE.readValue(DataType.DOUBLE, json.get("value")));
		} catch(Exception e) {
			TradersLogger.logError("Incorrect parameters for RandomCondition", e);
		}
		return null;
	}

}
