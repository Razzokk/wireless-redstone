package rzk.wirelessredstone.network;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.WRClientEventsNeo;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.TranslationKeys;

public class ModNetworking
{
	private static Void disconnect(IPayloadContext ctx, Throwable e) {
		ctx.disconnect(Component.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
		return null;
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event)
	{
		var registrar = event.registrar(WirelessRedstone.MOD_ID);

		registrar.playBidirectional(FrequencyBlockPacket.TYPE, FrequencyBlockPacket.STREAM_CODEC, new DirectionalPayloadHandler<>(
				(packet, ctx) -> ctx
					.enqueueWork(() -> ModScreens.openBlockFrequencyScreen(packet.frequency(), packet.pos()))
					.exceptionally(e -> disconnect(ctx, e)),
				(packet, ctx) -> ctx
					.enqueueWork(() -> {
						var level = ctx.player().level();
						var pos = packet.pos();
						if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
							block.setFrequency(level, pos, packet.frequency());
					})
					.exceptionally(e -> disconnect(ctx, e))
			));

		registrar.playBidirectional(FrequencyItemPacket.TYPE, FrequencyItemPacket.STREAM_CODEC, new DirectionalPayloadHandler<>(
			(packet, ctx) -> ctx
				.enqueueWork(() -> ModScreens.openItemFrequencyScreen(packet.frequency(), packet.hand()))
				.exceptionally(e -> disconnect(ctx, e)),
			(packet, ctx) -> ctx
				.enqueueWork(() -> {
					var stack = ctx.player().getItemInHand(packet.hand());
					if (stack.getItem() instanceof FrequencyItem)
						Frequency.set(stack, packet.frequency());
				})
				.exceptionally(e -> disconnect(ctx, e))
			));

		registrar.playToClient(SnifferHighlightPacket.TYPE, SnifferHighlightPacket.STREAM_CODEC, (packet, ctx) -> ctx
			.enqueueWork(() -> WRClientEventsNeo.handleSnifferHighlightPacket(packet.timestamp(), packet.hand(), packet.coords()))
			.exceptionally(e -> disconnect(ctx, e)));
	}
}
