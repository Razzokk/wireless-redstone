package rzk.wirelessredstone.client.screen;

import net.minecraft.core.BlockPos;
import rzk.wirelessredstone.client.WirelessRedstoneClient;
import rzk.wirelessredstone.network.FrequencyBlockPacket;

public class FrequencyBlockScreen extends FrequencyScreen
{
	private final BlockPos pos;

	public FrequencyBlockScreen(int frequency, BlockPos pos)
	{
		super(frequency);
		this.pos = pos;
	}

	@Override
	protected void setFrequency()
	{
		var packet = new FrequencyBlockPacket(getInputFrequency(), pos);
		WirelessRedstoneClient.PLATFORM.sendPacket(packet);
	}
}
