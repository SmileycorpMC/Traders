package net.smileycorp.traders.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.smileycorp.traders.common.entities.EntityWanderingTrader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandSpawnTrader extends CommandBase {
    
    
    @Override
    public String getName() {
        return "spawnTrader";
    }
    
    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "commands." + Constants.MODID + ".spawnTrader.usage";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = args.length == 0 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
        WorldServer world = (WorldServer) sender.getEntityWorld();
        EntityWanderingTrader trader = WanderingTraderSpawner.getSpawner(world).spawn(world, player);
        if (trader == null) throw new CommandException("commands." + Constants.MODID + ".spawnTrader.failure");
        BlockPos pos = trader.getPosition();
        notifyCommandListener(sender, this, "commands." + Constants.MODID + ".spawnTrader.success",
                pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
    }
    
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
    
}
