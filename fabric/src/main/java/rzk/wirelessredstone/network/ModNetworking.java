package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.registry.ModItems;

public class ModNetworking
{
	public static void register()
	{
		ServerPlayNetworking.registerGlobalReceiver(FrequencyBlockPacket.TYPE, (packet, player, responseSender) ->
		{
			var level = player.level();
			if (level.getBlockState(packet.pos()).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(level, packet.pos(), packet.frequency());
		});

		ServerPlayNetworking.registerGlobalReceiver(FrequencyItemPacket.TYPE, (packet, player, responseSender) ->
		{
			var stack = player.getItemInHand(packet.hand());
			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, packet.frequency());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
		{
			var player = handler.player;
			var stack = player.getUseItem();
			if (!stack.is(ModItems.remote)) return;
			((RemoteItem) stack.getItem()).onDeactivation(stack, player.level(), player);
		});
	}
}
