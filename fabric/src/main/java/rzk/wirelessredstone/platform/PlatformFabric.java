package rzk.wirelessredstone.platform;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;

public class PlatformFabric implements Platform {
	@Override
	public PlatformLoader getLoader() {
		return PlatformLoader.FABRIC;
	}

	@Override
	public File getConfigDir() {
		return FabricLoader.getInstance().getConfigDir().toFile();
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public void sendPacket(ServerPlayer player, CustomPacketPayload packet) {
		ServerPlayNetworking.send(player, packet);
	}
}
