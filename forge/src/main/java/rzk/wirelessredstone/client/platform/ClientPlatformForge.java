package rzk.wirelessredstone.client.platform;

import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.network.ModNetworking;
import rzk.wirelessredstone.network.Packet;

public class ClientPlatformForge implements ClientPlatform {
	@Override
	public void sendPacket(Packet packet) {
		ModNetworking.INSTANCE.send(packet, PacketDistributor.SERVER.noArg());
	}
}
