package rzk.wirelessredstone.client.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClientPlatformNeo implements ClientPlatform
{
	@Override
	public void sendPacket(CustomPacketPayload packet) {
		PacketDistributor.sendToServer(packet);
	}
}
