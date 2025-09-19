package rzk.wirelessredstone.client.screen;

import net.minecraft.core.BlockPos;
import rzk.wirelessredstone.client.WirelessRedstoneClient;

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
		WirelessRedstoneClient.PLATFORM.sendFrequencyBlockPacket(getInputFrequency(), pos);
	}
}
