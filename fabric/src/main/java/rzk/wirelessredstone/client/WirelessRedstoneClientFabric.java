package rzk.wirelessredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import rzk.wirelessredstone.client.render.HudRenderer;
import rzk.wirelessredstone.client.render.WorldOverlayRendererFabric;
import rzk.wirelessredstone.client.screen.FrequencyBlockScreen;
import rzk.wirelessredstone.client.screen.FrequencyItemScreen;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.Packet;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

@Environment(EnvType.CLIENT)
public class WirelessRedstoneClientFabric implements ClientModInitializer {
	@FunctionalInterface
	public interface PacketHandler<T extends Packet> {
		void receive(T packet, LocalPlayer player);
	}

	public static <T extends Packet> boolean registerGlobalReceiver(Packet.Type<T> type, PacketHandler<T> receiver) {
		return ClientPlayNetworking.registerGlobalReceiver(type.id(), (client, handler, buf, responseSender) -> {
			var packet = type.creator().apply(buf);
			if (client.isSameThread()) {
				receiver.receive(packet, client.player);
			}
			else {
				client.execute(() -> {
					if (handler.isAcceptingMessages()) receiver.receive(packet, client.player);
				});
			}
		});
	}

	@Override
	public void onInitializeClient() {
		WirelessRedstoneClient.registerBlockEntityRenderers();
		WirelessRedstoneClient.registerItemProperties();

		WorldRenderEvents.AFTER_TRANSLUCENT.register(WorldOverlayRendererFabric::render);
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> HudRenderer.renderP2pTarget(drawContext));

		registerGlobalReceiver(SnifferHighlightPacket.TYPE, (packet, player) -> {
			var stack = player.getItemInHand(packet.hand());
			SnifferItem.setHighlightedBlocks(packet.timestamp(), stack, packet.coords());
		});

		registerGlobalReceiver(FrequencyBlockPacket.TYPE, (packet, player) ->
			Minecraft.getInstance().setScreen(new FrequencyBlockScreen(packet.frequency(), packet.pos())));

		registerGlobalReceiver(FrequencyItemPacket.TYPE, (packet, player) ->
			Minecraft.getInstance().setScreen(new FrequencyItemScreen(packet.frequency(), packet.hand())));
	}
}
