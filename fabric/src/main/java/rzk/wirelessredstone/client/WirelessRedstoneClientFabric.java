package rzk.wirelessredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import rzk.wirelessredstone.client.render.WorldOverlayRendererFabric;
import rzk.wirelessredstone.client.screen.FrequencyBlockScreen;
import rzk.wirelessredstone.client.screen.FrequencyItemScreen;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

@Environment(EnvType.CLIENT)
public class WirelessRedstoneClientFabric implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		WirelessRedstoneClient.registerBlockEntityRenderers();
		WirelessRedstoneClient.registerItemProperties();

		WorldRenderEvents.AFTER_TRANSLUCENT.register(WorldOverlayRendererFabric::render);

		ClientPlayNetworking.registerGlobalReceiver(SnifferHighlightPacket.TYPE, (packet, player, responseSender) ->
		{
			var stack = player.getItemInHand(packet.hand());
			SnifferItem.setHighlightedBlocks(packet.timestamp(), stack, packet.coords());
		});

		ClientPlayNetworking.registerGlobalReceiver(FrequencyBlockPacket.TYPE, (packet, player, responseSender) ->
			Minecraft.getInstance().setScreen(new FrequencyBlockScreen(packet.frequency(), packet.pos())));

		ClientPlayNetworking.registerGlobalReceiver(FrequencyItemPacket.TYPE, (packet, player, responseSender) ->
			Minecraft.getInstance().setScreen(new FrequencyItemScreen(packet.frequency(), packet.hand())));

	}
}
