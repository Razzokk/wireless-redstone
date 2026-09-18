package rzk.wirelessredstone.platform;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;
import rzk.wirelessredstone.network.Packet;

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
	public void sendPacket(ServerPlayer player, Packet packet) {
		var buf = PacketByteBufs.create();
		packet.write(buf);
		ServerPlayNetworking.send(player, packet.type().id(), buf);
	}
}
