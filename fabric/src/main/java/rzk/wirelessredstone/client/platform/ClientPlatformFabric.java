package rzk.wirelessredstone.client.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;

public class ClientPlatformFabric implements ClientPlatform
{
	@Override
	public void sendFrequencyItemPacket(int frequency, InteractionHand hand)
	{
		ClientPlayNetworking.send(new FrequencyItemPacket(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(int frequency, BlockPos pos)
	{
		ClientPlayNetworking.send(new FrequencyBlockPacket(frequency, pos));
	}
}
