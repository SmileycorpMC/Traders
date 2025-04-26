package net.smileycorp.traders.config.condition;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.smileycorp.atlas.api.data.LogicalOperation;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;

import java.util.List;

public class LogicalCondition implements TradeCondition {

	protected final LogicalOperation operation;
	protected final TradeCondition[] conditions;

	private LogicalCondition(LogicalOperation operation, TradeCondition... conditions) {
		this.operation = operation;
		this.conditions = conditions;
	}

	@Override
	public boolean apply(TradeContext ctx) {
		boolean result = false;
		for (TradeCondition condition : conditions) result = operation.apply(result, condition.apply(ctx));
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < conditions.length; i++) {
			builder.append(conditions[i].toString());
			if (i < conditions.length-1) builder.append(" " + operation.getSymbol() + " ");
		}
		return super.toString() + "[" + builder + "]";
	}

	public static LogicalCondition deserialize(LogicalOperation operation, JsonObject json) {
		try {
			List<TradeCondition> conditions = Lists.newArrayList();
			for (JsonElement element : json.get("value").getAsJsonArray()) {
				try {
					conditions.add(ConditionRegistry.INSTANCE.readCondition(element.getAsJsonObject()));
				} catch(Exception e) {
					TradersLogger.logError("Failed to read condition of logical " + element, e);
				}
			}
			return new LogicalCondition(operation, conditions.toArray(new TradeCondition[conditions.size()]));
		} catch(Exception e) {
			TradersLogger.logError("Incorrect parameters for LogicalCondition " + operation.getName(), e);
		}
		return null;
	}

}
