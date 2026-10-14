package rzk.wirelessredstone.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.WRClientEventsForge;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.Frequency;

public class ModNetworking
{
	public static final SimpleChannel INSTANCE = ChannelBuilder
		.named(new ResourceLocation(WirelessRedstone.MOD_ID, "main"))
		.simpleChannel();

	public static void registerMessages()
	{
		INSTANCE.messageBuilder(FrequencyBlockPacket.class)
			.codec(FrequencyBlockPacket.STREAM_CODEC)
			.consumerMainThread(
				(packet, ctx) -> {
					if (ctx.isClientSide()) {
						ctx.enqueueWork(() -> ModScreens.openBlockFrequencyScreen(packet.frequency(), packet.pos()));
					}
					else if (ctx.isServerSide()) {
						ctx.enqueueWork(() -> {
							var level = ctx.getSender().level();
							var pos = packet.pos();
							if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
								block.setFrequency(level, pos, packet.frequency());
						});
					}
				}
			)
			.add();

		INSTANCE.messageBuilder(FrequencyItemPacket.class)
			.codec(FrequencyItemPacket.STREAM_CODEC)
			.consumerMainThread(
				(packet, ctx) -> {
					if (ctx.isClientSide()) {
						ctx.enqueueWork(() -> ModScreens.openItemFrequencyScreen(packet.frequency(), packet.hand()));
					}
					else if (ctx.isServerSide()) {
						ctx.enqueueWork(() -> {
							var stack = ctx.getSender().getItemInHand(packet.hand());
							if (stack.getItem() instanceof FrequencyItem)
								Frequency.set(stack, packet.frequency());
						});
					}
				}
			)
			.add();

		INSTANCE.messageBuilder(SnifferHighlightPacket.class, NetworkDirection.PLAY_TO_CLIENT)
			.codec(SnifferHighlightPacket.STREAM_CODEC)
			.consumerMainThread((packet, ctx) ->
				ctx.enqueueWork(() -> WRClientEventsForge.handleSnifferHighlightPacket(packet.timestamp(), packet.hand(), packet.coords()))
			)
			.add();
	}
}
