package rzk.wirelessredstone.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.P2pRedstoneReceiverBlock;
import rzk.wirelessredstone.block.P2pRedstoneTransmitterBlock;
import rzk.wirelessredstone.block.RedstoneReceiverBlock;
import rzk.wirelessredstone.block.RedstoneTransmitterBlock;

public final class ModBlocks
{
	public static Block redstoneTransmitter;
	public static Block redstoneReceiver;
	public static Block p2pRedstoneTransmitter;
	public static Block p2pRedstoneReceiver;

	public static void register()
	{
		redstoneTransmitter = registerBlock("redstone_transmitter", new RedstoneTransmitterBlock());
		redstoneReceiver = registerBlock("redstone_receiver", new RedstoneReceiverBlock());
		p2pRedstoneTransmitter = registerBlock("p2p_redstone_transmitter", new P2pRedstoneTransmitterBlock());
		p2pRedstoneReceiver = registerBlock("p2p_redstone_receiver", new P2pRedstoneReceiverBlock());
	}

	private static Block registerBlock(String name, Block block)
	{
		var resourceLocation = new ResourceLocation(WirelessRedstone.MOD_ID, name);
		Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block);
		return block;
	}
}
