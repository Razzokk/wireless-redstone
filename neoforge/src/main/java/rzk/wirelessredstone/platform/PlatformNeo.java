package rzk.wirelessredstone.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

import java.io.File;

public class PlatformNeo implements Platform
{
	@Override
	public PlatformLoader getLoader()
	{
		return PlatformLoader.NEOFORGE;
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
		PacketDistributor.PLAYER.with(player).send(new FrequencyItemPacket(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(ServerPlayer player, int frequency, BlockPos pos)
	{
		PacketDistributor.PLAYER.with(player).send(new FrequencyBlockPacket(frequency, pos));
	}

	@Override
	public void sendSniffer(ServerPlayer player, long time, InteractionHand hand, BlockPos[] transmitters)
	{
		PacketDistributor.PLAYER.with(player).send(new SnifferHighlightPacket(time, hand, transmitters));
	}
}
