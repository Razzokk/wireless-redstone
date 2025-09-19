package rzk.wirelessredstone.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rzk.wirelessredstone.WirelessRedstone;

public class ModNetworking
{
	private static final String PROTOCOL_VERSION = "1.0";
	private static int id = 0;

	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(WirelessRedstone.MOD_ID, "main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	private static int id()
	{
		return id++;
	}

	public static void registerMessages()
	{
		INSTANCE.messageBuilder(FrequencyBlockPacket.class, id())
			.encoder(FrequencyBlockPacket::write)
			.decoder(FrequencyBlockPacket::new)
			.consumerMainThread(FrequencyBlockPacket::handle)
			.add();

		INSTANCE.messageBuilder(FrequencyItemPacket.class, id())
			.encoder(FrequencyItemPacket::write)
			.decoder(FrequencyItemPacket::new)
			.consumerMainThread(FrequencyItemPacket::handle)
			.add();

		INSTANCE.messageBuilder(SnifferHighlightPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
			.encoder(SnifferHighlightPacket::write)
			.decoder(SnifferHighlightPacket::new)
			.consumerMainThread(SnifferHighlightPacket::handle)
			.add();
	}
}
