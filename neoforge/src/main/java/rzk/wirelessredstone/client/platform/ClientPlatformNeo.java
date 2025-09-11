package rzk.wirelessredstone.client.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;

public class ClientPlatformNeo implements ClientPlatform
{
	@Override
	public void sendFrequencyItemPacket(int frequency, InteractionHand hand)
	{
		PacketDistributor.SERVER.noArg().send(new FrequencyItemPacket(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(int frequency, BlockPos pos)
	{
		PacketDistributor.SERVER.noArg().send(new FrequencyBlockPacket(frequency, pos));
	}
}
