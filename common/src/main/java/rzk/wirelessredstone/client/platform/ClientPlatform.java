package rzk.wirelessredstone.client.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;

import java.util.ServiceLoader;

public interface ClientPlatform
{
	static ClientPlatform load()
	{
		var platforms = ServiceLoader.load(ClientPlatform.class);
		return platforms.findFirst().orElseThrow(() -> new RuntimeException("Couldn't find wireless redstone client platform!"));
	}

	void sendFrequencyItemPacket(int frequency, InteractionHand hand);

	void sendFrequencyBlockPacket(int frequency, BlockPos pos);
}
