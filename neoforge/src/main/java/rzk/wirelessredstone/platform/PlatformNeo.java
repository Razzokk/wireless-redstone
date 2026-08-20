package rzk.wirelessredstone.platform;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;
import rzk.wirelessredstone.network.Packet;

import java.io.File;

public class PlatformNeo implements Platform {
	@Override
	public PlatformLoader getLoader() {
		return PlatformLoader.NEOFORGE;
	}

	@Override
	public File getConfigDir() {
		return FMLPaths.CONFIGDIR.get().toFile();
	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public void sendPacket(ServerPlayer player, Packet packet) {
		PacketDistributor.PLAYER.with(player).send(packet);
	}
}
