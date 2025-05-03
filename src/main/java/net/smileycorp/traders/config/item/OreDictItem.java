package net.smileycorp.traders.config.item;

import com.google.gson.JsonObject;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.traders.config.item.functions.ItemFunction;
import net.smileycorp.traders.config.trades.TradeContext;
import net.smileycorp.traders.config.values.Value;
import net.smileycorp.traders.config.values.ValueRegistry;

public class OreDictItem implements TradeItem {
    
    private final Value<String> ore, nbt;
    private final Value<Integer> count;
    private final ItemFunction[] functions;
    private final String json;
    
    OreDictItem(String json, Value<String> value, Value<Integer> count, Value<String> nbt, ItemFunction... functions) {
        this.ore = value;
        this.count = count;
        this.nbt = nbt;
        this.functions = functions;
        this.json = json;
    }
    
    @Override
    public ItemStack get(TradeContext ctx) {
        NonNullList<ItemStack> stacks = OreDictionary.getOres(ore.get(ctx));
        if (stacks.isEmpty()) return ItemStack.EMPTY;
        ItemStack stack = stacks.get(ctx.getTrader().getRNG().nextInt(stacks.size()));
        try {
            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                NonNullList<ItemStack> items = NonNullList.create();
                stack.getItem().getSubItems(CreativeTabs.SEARCH, items);
                stack = new ItemStack(stack.getItem(), 1, items.isEmpty() ? 1 : ctx.getTrader().getRNG().nextInt(items.size()));
            }
            stack.setCount(MathHelper.clamp(count.get(ctx), 1, stack.getMaxStackSize()));
            NBTTagCompound nbt = JsonToNBT.getTagFromJson(this.nbt.get(ctx));
            if (nbt != null &! nbt.hasNoTags()) stack.setTagCompound(nbt);
        } catch (Exception e) {}
        for (ItemFunction func :  functions) stack = func.apply(stack, ctx);
        return stack;
    }
    
    @Override
    public String toString() {
        return json;
    }
    
    public static TradeItem deserialize(JsonObject json, Value<Integer> count, Value<String> nbt, ItemFunction... functions) {
        try {
            Value<String> ore = ValueRegistry.INSTANCE.readValue(DataType.STRING, json.get("ore"));
            return new OreDictItem(json.toString(), ore, count, nbt, functions);
        } catch (Exception e) {}
        return EMPTY;
    }
    
}
