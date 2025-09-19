package rzk.wirelessredstone.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.ModNetworking;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

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
	public void sendFrequencyItemPacket(ServerPlayer player, int frequency, InteractionHand hand)
	{

		ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new FrequencyItemPacket(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(ServerPlayer player, int frequency, BlockPos pos)
	{
		ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new FrequencyBlockPacket(frequency, pos));
	}

	@Override
	public void sendSniffer(ServerPlayer player, long time, InteractionHand hand, BlockPos[] transmitters)
	{
		ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SnifferHighlightPacket(time, hand, transmitters));
	}
}
