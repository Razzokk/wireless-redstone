package rzk.wirelessredstone.misc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.registry.ModItems;

public class WREvents
{
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		var player = event.getEntity();
		var level = player.level();
		var stack = player.getUseItem();
		if (level.isClientSide || !stack.is(ModItems.remote)) return;
		((RemoteItem) stack.getItem()).onDeactivation(stack, level, player);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event)
	{
		if (!(event.getLevel() instanceof ServerLevel level)) return;
		if (!(event.getChunk() instanceof LevelChunk chunk)) return;
		WirelessRedstone.onChunkLoad(level, chunk);
	}

	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event)
	{
		if (!(event.getLevel() instanceof ServerLevel level)) return;
		if (!(event.getChunk() instanceof LevelChunk chunk)) return;
		WirelessRedstone.onChunkUnload(level, chunk);
	}
}
