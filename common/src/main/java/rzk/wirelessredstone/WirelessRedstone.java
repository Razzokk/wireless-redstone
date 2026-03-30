package rzk.wirelessredstone;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.api.ChunkLoadListener;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.platform.Platform;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public final class WirelessRedstone
{
	public static final String MOD_ID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Platform PLATFORM = Platform.load();

	private static final CreativeModeTab CREATIVE_MODE_TAB = CreativeModeTab.builder(null, 0)
		.displayItems((params, entries) ->
		{
			entries.accept(ModBlocks.redstoneTransmitter);
			entries.accept(ModBlocks.redstoneReceiver);
			entries.accept(ModBlocks.p2pRedstoneTransmitter);
			entries.accept(ModBlocks.p2pRedstoneReceiver);
			entries.accept(ModBlocks.redstoneTransmitterAttachment);
			entries.accept(ModBlocks.redstoneReceiverAttachment);
			entries.accept(ModItems.circuit);
			entries.accept(ModItems.frequencyTool);
			entries.accept(ModItems.frequencySniffer);
			entries.accept(ModItems.remote);
			entries.accept(ModItems.linker);
		})
		.title(Component.translatable(TranslationKeys.ITEM_GROUP_WIRELESS_REDSTONE))
		.icon(() -> new ItemStack(ModBlocks.redstoneTransmitter))
		.build();

	public static void registerCreativeTab()
	{
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(WirelessRedstone.MOD_ID, WirelessRedstone.MOD_ID), CREATIVE_MODE_TAB);
	}

	public static void onChunkLoad(ServerLevel level, LevelChunk chunk)
	{
		var server = level.getServer();
		server.tell(new TickTask(server.getTickCount() + 1, () ->
		{
			var blockEntities = chunk.getBlockEntities().values();
			for (var blockEntity : blockEntities)
				if (blockEntity instanceof ChunkLoadListener listener)
					listener.onChunkLoad(level);
		}));
	}

	public static void onChunkUnload(ServerLevel level, LevelChunk chunk)
	{
		var blockEntities = chunk.getBlockEntities().values();
		for (var blockEntity : blockEntities)
			if (blockEntity instanceof ChunkLoadListener listener)
				listener.onChunkUnload(level);
	}
}
