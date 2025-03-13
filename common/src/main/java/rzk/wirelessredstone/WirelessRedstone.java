package rzk.wirelessredstone;

import net.minecraft.server.ServerTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.api.ChunkLoadListener;
import rzk.wirelessredstone.platform.Platform;

import java.util.ServiceLoader;

public final class WirelessRedstone
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Platform PLATFORM = load();

	private WirelessRedstone() {}

	public static Identifier identifier(String path)
	{
		return new Identifier(MODID, path);
	}

	private static Platform load()
	{
		var platforms = ServiceLoader.load(Platform.class);
		return platforms.findFirst().orElseThrow(() -> new RuntimeException("Couldn't find wireless redstone platform!"));
	}

	public static void onChunkLoad(ServerWorld world, WorldChunk chunk)
	{
		var server = world.getServer();
		server.send(new ServerTask(server.getTicks() + 1, () ->
		{
			var blockEntities = chunk.getBlockEntities().values();
			for (var blockEntity : blockEntities)
				if (blockEntity instanceof ChunkLoadListener listener)
					listener.onChunkLoad(world);
		}));
	}

	public static void onChunkUnload(ServerWorld world, WorldChunk chunk)
	{
		var blockEntities = chunk.getBlockEntities().values();
		for (var blockEntity : blockEntities)
			if (blockEntity instanceof ChunkLoadListener listener)
				listener.onChunkUnload(world);
	}
}
