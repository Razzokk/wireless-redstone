package rzk.wirelessredstone.client.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.ModNetworking;

public class ClientPlatformForge implements ClientPlatform
{
	@Override
	public void sendFrequencyItemPacket(int frequency, InteractionHand hand)
	{
		ModNetworking.INSTANCE.send(new FrequencyItemPacket(frequency, hand), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void sendFrequencyBlockPacket(int frequency, BlockPos pos)
	{
		ModNetworking.INSTANCE.send(new FrequencyBlockPacket(frequency, pos), PacketDistributor.SERVER.noArg());
	}
}
