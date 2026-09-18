package rzk.wirelessredstone.client.platform;

import rzk.wirelessredstone.network.Packet;

import java.util.ServiceLoader;

public interface ClientPlatform {
	static ClientPlatform load() {
		var platforms = ServiceLoader.load(ClientPlatform.class);
		return platforms.findFirst().orElseThrow(() -> new RuntimeException("Couldn't find wireless redstone client platform!"));
	}

	void sendPacket(Packet packet);
}
