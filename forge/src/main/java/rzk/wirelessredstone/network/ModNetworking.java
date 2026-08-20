package rzk.wirelessredstone.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.WRClientEventsForge;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.Frequency;

import java.util.function.BiConsumer;

public class ModNetworking
{
	public static final SimpleChannel INSTANCE = ChannelBuilder
		.named(new ResourceLocation(WirelessRedstone.MOD_ID, "main"))
		.simpleChannel();

	private static <T extends Packet> void registerMessage(Class<T> clazz, Packet.Type<T> type, NetworkDirection direction, BiConsumer<T, CustomPayloadEvent.Context> consumer) {
		INSTANCE.messageBuilder(clazz, direction)
			.encoder(Packet::write)
			.decoder(type.reader())
			.consumerMainThread(consumer)
			.add();
	}

	private static <T extends Packet> void registerMessage(Class<T> clazz, Packet.Type<T> type, BiConsumer<T, CustomPayloadEvent.Context> consumer) {
		registerMessage(clazz, type, null, consumer);
	}

	public static void registerMessages()
	{
		registerMessage(FrequencyBlockPacket.class, FrequencyBlockPacket.TYPE, (packet, context) -> {
			var pos = packet.pos();
			var frequency = packet.frequency();

			if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
				var level = context.getSender().level();
				if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
					block.setFrequency(level, pos, frequency);
			}
			else {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openBlockFrequencyScreen(frequency, pos));
			}
		});

		registerMessage(FrequencyItemPacket.class, FrequencyItemPacket.TYPE, (packet, context) -> {
			var hand = packet.hand();
			var frequency = packet.frequency();

			if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
				var player = context.getSender();
				var stack = player.getItemInHand(hand);
				if (stack.getItem() instanceof FrequencyItem)
					Frequency.set(stack, frequency);
			}
			else {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openItemFrequencyScreen(frequency, hand));
			}
		});

		registerMessage(SnifferHighlightPacket.class, SnifferHighlightPacket.TYPE, NetworkDirection.PLAY_TO_CLIENT, (packet, context) -> {
			var timestamp = packet.timestamp();
			var hand = packet.hand();
			var coords = packet.coords();
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WRClientEventsForge.handleSnifferHighlightPacket(timestamp, hand, coords));
		});
	}
}
