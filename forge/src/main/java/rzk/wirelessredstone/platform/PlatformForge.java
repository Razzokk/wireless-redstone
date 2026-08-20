package rzk.wirelessredstone.platform;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.network.Packet;
import rzk.wirelessredstone.network.ModNetworking;

import java.io.File;

public class PlatformForge implements Platform
{
	@Override
	public PlatformLoader getLoader()
	{
		return PlatformLoader.FORGE;
	}

	@Override
	public File getConfigDir()
	{
		return FMLPaths.CONFIGDIR.get().toFile();
	}

	@Override
	public boolean isModLoaded(String modId)
	{
		return ModList.get().isLoaded(modId);
	}

	@Override
	public void sendPacket(ServerPlayer player, Packet packet) {
		ModNetworking.INSTANCE.send(packet, PacketDistributor.PLAYER.with(player));
	}
}
