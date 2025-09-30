package rzk.wirelessredstone.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.P2pRedstoneReceiverBlock;
import rzk.wirelessredstone.block.P2pRedstoneTransmitterBlock;
import rzk.wirelessredstone.block.RedstoneReceiverBlock;
import rzk.wirelessredstone.block.RedstoneTransmitterBlock;
import rzk.wirelessredstone.misc.RegisterUtil;

public final class ModBlocks
{
	public static Block redstoneTransmitter;
	public static Block redstoneReceiver;
	public static Block p2pRedstoneTransmitter;
	public static Block p2pRedstoneReceiver;

	public static void register(RegisterUtil<Block> util)
	{
		redstoneTransmitter = registerBlock(util, "redstone_transmitter", new RedstoneTransmitterBlock());
		redstoneReceiver = registerBlock(util, "redstone_receiver", new RedstoneReceiverBlock());
		p2pRedstoneTransmitter = registerBlock(util, "p2p_redstone_transmitter", new P2pRedstoneTransmitterBlock());
		p2pRedstoneReceiver = registerBlock(util, "p2p_redstone_receiver", new P2pRedstoneReceiverBlock());
	}

	private static Block registerBlock(RegisterUtil<Block> util, String name, Block block)
	{
		var resourceLocation = new ResourceLocation(WirelessRedstone.MOD_ID, name);
		util.register(resourceLocation, block);
		return block;
	}
}
