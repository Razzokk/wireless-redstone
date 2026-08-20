package rzk.wirelessredstone.client.screen;

import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.client.WirelessRedstoneClient;
import rzk.wirelessredstone.network.FrequencyItemPacket;

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
		var packet = new FrequencyItemPacket(getInputFrequency(), hand);
		WirelessRedstoneClient.PLATFORM.sendPacket(packet);
	}
}
