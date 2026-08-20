package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.registry.ModItems;

public class ModNetworking {
	@FunctionalInterface
	public interface PacketHandler<T extends Packet> {
		void receive(T packet, ServerPlayer player);
	}

	public static <T extends Packet> boolean registerGlobalReceiver(Packet.Type<T> type, PacketHandler<T> receiver) {
		return ServerPlayNetworking.registerGlobalReceiver(type.id(), (server, player, handler, buf, responseSender) -> {
			var packet = type.reader().apply(buf);
			if (server.isSameThread()) {
				receiver.receive(packet, player);
			}
			else {
				server.execute(() -> {
					if (handler.isAcceptingMessages()) receiver.receive(packet, player);
				});
			}
		});
	}

	public static void register() {
		registerGlobalReceiver(FrequencyBlockPacket.TYPE, (packet, player) -> {
			var level = player.level();
			if (level.getBlockState(packet.pos()).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(level, packet.pos(), packet.frequency());
		});

		registerGlobalReceiver(FrequencyItemPacket.TYPE, (packet, player) -> {
			var stack = player.getItemInHand(packet.hand());
			if (stack.getItem() instanceof FrequencyItem)
				Frequency.set(stack, packet.frequency());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			var player = handler.player;
			var stack = player.getUseItem();
			if (!stack.is(ModItems.remote)) return;
			((RemoteItem) stack.getItem()).onDeactivation(stack, player.level(), player);
		});
	}
}
