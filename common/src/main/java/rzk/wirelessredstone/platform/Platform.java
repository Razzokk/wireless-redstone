package rzk.wirelessredstone.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

import java.io.File;
import java.util.ServiceLoader;

public interface Platform
{
	static Platform load()
	{
		var platforms = ServiceLoader.load(Platform.class);
		return platforms.findFirst().orElseThrow(() -> new RuntimeException("Couldn't find wireless redstone platform!"));
	}

	PlatformLoader getLoader();

	File getConfigDir();

	boolean isModLoaded(String modId);

	void sendFrequencyItemPacket(ServerPlayer player, int frequency, InteractionHand hand);

	void sendFrequencyBlockPacket(ServerPlayer player, int frequency, BlockPos pos);

	void sendSniffer(ServerPlayer player, long time, InteractionHand hand, BlockPos[] transmitters);
}
