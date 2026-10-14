package rzk.wirelessredstone.client.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ClientPlatformFabric implements ClientPlatform {
	@Override
	public void sendPacket(CustomPacketPayload packet) {
		ClientPlayNetworking.send(packet);
	}
}
