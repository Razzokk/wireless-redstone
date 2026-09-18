package rzk.wirelessredstone.client.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import rzk.wirelessredstone.network.Packet;

public class ClientPlatformFabric implements ClientPlatform {
	@Override
	public void sendPacket(Packet packet) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		packet.write(buf);
		ClientPlayNetworking.send(packet.type().id(), buf);
	}
}
