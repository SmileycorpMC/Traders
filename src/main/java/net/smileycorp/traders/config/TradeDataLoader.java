package net.smileycorp.traders.config;

import com.google.common.collect.Lists;
import com.google.gson.JsonParser;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.traders.common.TradersLogger;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.util.List;

public class TradeDataLoader {

    public static TradeDataLoader INSTANCE;
    private final File directory;
    private final List<TradeTable> tables = Lists.newArrayList();
    
    public static void init(FMLPreInitializationEvent event) {
        INSTANCE = new TradeDataLoader(new File(event.getModConfigurationDirectory().getPath() + "/traders/trades"));
    }
    
    private TradeDataLoader(File directory) {
        this.directory = directory;
        if (!directory.exists()) {
            TradersLogger.logInfo("Raid table folder does not exist, generating default data");
            directory.mkdirs();
            try {
                FileUtils.copyInputStreamToFile(TradeDataLoader.class.getResourceAsStream("/config-defaults/trades/buying.json"),
                        new File(directory, "buying.json"));
                FileUtils.copyInputStreamToFile(TradeDataLoader.class.getResourceAsStream("/config-defaults/trades/selling.json"),
                        new File(directory, "selling.json"));
                FileUtils.copyInputStreamToFile(TradeDataLoader.class.getResourceAsStream("/config-defaults/trades/special.json"),
                        new File(directory, "special.json"));
            } catch (Exception e) {
                TradersLogger.logError("Failed generating default trades", e);
            }
        }
    }
    
    public void loadTrades() {
        JsonParser parser = new JsonParser();
        for (File file : directory.listFiles((f, s) -> s.endsWith(".json"))) {
            try {
                tables.add(TradeTable.deserialize(parser.parse(new FileReader(file))));
                TradersLogger.logInfo("Loaded trade table " + file.getName());
            } catch (Exception e) {
                TradersLogger.logError("Failed loading trade table " + file.getName(), e);
            }
        }
    }
    
    public MerchantRecipeList getTrades(EntityWanderingTrader trader) {
        TradeContext ctx = new TradeContext(trader);
        MerchantRecipeList trades = new MerchantRecipeList();
        for (TradeTable table : tables) if (table.canApply(ctx)) trades.addAll(table.getTrades(ctx));
        return trades;
    }

}
