package rzk.wirelessredstone.client.platform;

import net.neoforged.neoforge.network.PacketDistributor;
import rzk.wirelessredstone.network.Packet;

public class ClientPlatformNeo implements ClientPlatform {
	@Override
	public void sendPacket(Packet packet) {
		PacketDistributor.SERVER.noArg().send(packet);
	}
}
