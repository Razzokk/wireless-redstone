package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IDirectionAwarePayloadHandlerBuilder;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.WRClientEventsNeo;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.TranslationKeys;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModNetworking {
	private static <T extends Packet> void register(IPayloadRegistrar registrar, Packet.Type<T> type, Consumer<IDirectionAwarePayloadHandlerBuilder<T, IPlayPayloadHandler<T>>> builder) {
		registrar.play(type.id(), type.reader(), builder);
	}

	private static <T extends Future<T>> Function<Throwable, T> failed(PlayPayloadContext ctx) {
		return e -> {
			ctx.packetHandler().disconnect(Component.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		};
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlerEvent event) {
		var registrar = event.registrar(WirelessRedstone.MOD_ID);

		register(registrar, FrequencyBlockPacket.TYPE, builder -> builder
			.server((packet, ctx) ->
				ctx.workHandler().submitAsync(() -> {
					var level = ctx.level().orElseThrow();
					var pos = packet.pos();
					if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
						block.setFrequency(level, pos, packet.frequency());
				}).exceptionally(failed(ctx)))
			.client((packet, ctx) ->
				ctx.workHandler().submitAsync(() -> {
					if (FMLEnvironment.dist == Dist.CLIENT)
						ModScreens.openBlockFrequencyScreen(packet.frequency(), packet.pos());
				}).exceptionally(failed(ctx)))
		);

		register(registrar, FrequencyItemPacket.TYPE, builder -> builder
			.server((packet, ctx) ->
				ctx.workHandler().submitAsync(() -> {
					var player = ctx.player().orElseThrow();
					var stack = player.getItemInHand(packet.hand());
					if (stack.getItem() instanceof FrequencyItem)
						Frequency.set(stack, packet.frequency());
				}).exceptionally(failed(ctx)))
			.client((packet, ctx) ->
				ctx.workHandler().submitAsync(() -> {
					if (FMLEnvironment.dist == Dist.CLIENT)
						ModScreens.openItemFrequencyScreen(packet.frequency(), packet.hand());
				}).exceptionally(failed(ctx)))
		);

		register(registrar, SnifferHighlightPacket.TYPE, builder -> builder
			.client((packet, ctx) ->
				ctx.workHandler().submitAsync(() -> {
					if (FMLEnvironment.dist == Dist.CLIENT)
						WRClientEventsNeo.handleSnifferHighlightPacket(packet.timestamp(), packet.hand(), packet.coords());
				}).exceptionally(failed(ctx)))
		);
	}
}
