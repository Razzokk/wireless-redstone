package rzk.wirelessredstone.api;

import net.minecraft.server.level.ServerLevel;

public interface ChunkLoadListener
{
	void onChunkLoad(ServerLevel level);

	void onChunkUnload(ServerLevel level);
}
