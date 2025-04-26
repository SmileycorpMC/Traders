package net.smileycorp.traders.config.values;

import com.google.gson.JsonObject;
import net.minecraft.command.CommandBase;
import net.minecraft.nbt.NBTTagCompound;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;
import net.smileycorp.traders.config.TradeContext;

public class TraderNBTValue<T extends Comparable<T>> extends NBTValue<T> {

	private TraderNBTValue(Value<String> value, DataType<T> type) {
		super(value, type);
	}

	@Override
	protected NBTTagCompound getNBT(TradeContext ctx) {
		EntityWanderingTrader trader = ctx.getTrader();
		if (trader == null) return new NBTTagCompound();
		return CommandBase.entityToNBT(trader);
	}
	
	public static <T extends Comparable<T>> TraderNBTValue<T> deserialize(JsonObject object, DataType<T> type) {
		try {
			if (object.has("value")) return new TraderNBTValue<T>(ValueRegistry.INSTANCE.readValue(DataType.STRING, object.get("value")), type);
		} catch (Exception e) {
			TradersLogger.logError("invalid value for TraderNBTValue", e);
		}
		return null;
	}

}
