package rzk.wirelessredstone.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ModItemsFabric;

public final class ModBlocksFabric
{
	private ModBlocksFabric() {}

	public static void registerBlocks()
	{
		ModBlocks.redstoneTransmitter = registerBlock("redstone_transmitter", new RedstoneTransmitterBlock());
		ModBlocks.redstoneReceiver = registerBlock("redstone_receiver", new RedstoneReceiverBlock());
	}

	public static Block registerBlockWithoutItem(String name, Block block)
	{
		Registry.register(Registries.BLOCK, WirelessRedstone.identifier(name), block);
		return block;
	}

	public static Block registerBlock(String name, Block block)
	{
		registerBlockWithoutItem(name, block);
		ModItemsFabric.registerItem(name, new BlockItem(block, new FabricItemSettings()));
		return block;
	}
}
