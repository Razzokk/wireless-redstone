package rzk.wirelessredstone.client.screen;

import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.client.WirelessRedstoneClient;

public class FrequencyItemScreen extends FrequencyScreen
{
	private final InteractionHand hand;

	public FrequencyItemScreen(int frequency, InteractionHand hand)
	{
		super(frequency);
		this.hand = hand;
	}

	@Override
	protected void setFrequency()
	{
		WirelessRedstoneClient.PLATFORM.sendFrequencyItemPacket(getInputFrequency(), hand);
	}
}
