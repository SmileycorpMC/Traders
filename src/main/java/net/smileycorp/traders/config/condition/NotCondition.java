package net.smileycorp.traders.config.condition;

import com.google.gson.JsonObject;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;

public class NotCondition implements TradeCondition {

	protected TradeCondition condition;

	public NotCondition(TradeCondition condition) {
		this.condition = condition;
	}

	@Override
	public boolean apply(TradeContext ctx) {
		return !condition.apply(ctx);
	}

	public static NotCondition deserialize(JsonObject json) {
		try {
			TradeCondition condition = ConditionRegistry.INSTANCE.readCondition(json.get("value").getAsJsonObject());
			if (condition != null) return new NotCondition(condition);
		} catch(Exception e) {
			TradersLogger.logError("Incorrect parameters for NotCondition", e);
		}
		return null;
	}

}
