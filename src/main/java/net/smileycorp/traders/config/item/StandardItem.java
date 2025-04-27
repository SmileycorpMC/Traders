package net.smileycorp.traders.config.item;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.config.TradeContext;
import net.smileycorp.traders.config.item.functions.ItemFunction;
import net.smileycorp.traders.config.values.StaticValue;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

public class StandardItem implements TradeItem {
    
    private final Value<String> item, nbt;
    private final Value<Integer> count, meta;
    private final ItemFunction[] functions;
    
    StandardItem(Value<String> item, Value<Integer> count, Value<Integer> meta, Value<String> nbt, ItemFunction... functions) {
        this.item = item;
        this.count = count;
        this.meta = meta;
        this.nbt = nbt;
        this.functions = functions;
    }
    
    @Override
    public ItemStack get(TradeContext ctx) {
        try {
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.item.get(ctx))), 1, meta.get(ctx));
            stack.setCount(MathHelper.clamp(count.get(ctx), 1, stack.getMaxStackSize()));
            try {
                NBTTagCompound nbt = JsonToNBT.getTagFromJson(this.nbt.get(ctx));
                if (nbt != null &! nbt.hasNoTags()) stack.setTagCompound(nbt);
            } catch (Exception e) {}
            for (ItemFunction func : functions) stack = func.apply(stack, ctx);
            return stack;
        } catch (Exception e) {
            TradersLogger.logError("Error creating item ", e);
        }
        return ItemStack.EMPTY;
    }
    
    public static TradeItem deserialize(JsonObject json, Value<Integer> count, Value<String> nbt, ItemFunction... functions) {
        try {
            Value<String> item = ValueRegistry.INSTANCE.readValue(DataType.STRING, json.get("item"));
            Value<Integer> meta = json.has("meta") ? ValueRegistry.INSTANCE.readValue(DataType.INT, json.get("meta")) : new StaticValue<>(0);
            return new StandardItem(item, count, meta, nbt, functions);
        } catch (Exception e) {}
        return EMPTY;
    }
    
}
