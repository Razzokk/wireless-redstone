package rzk.wirelessredstone.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.util.ServiceLoader;

public interface Platform {
	static Platform load() {
		var platforms = ServiceLoader.load(Platform.class);
		return platforms.findFirst().orElseThrow(() -> new RuntimeException("Couldn't find wireless redstone platform!"));
	}

	PlatformLoader getLoader();

	File getConfigDir();

	boolean isModLoaded(String modId);

	void sendPacket(ServerPlayer player, CustomPacketPayload packet);
}
