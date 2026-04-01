package rzk.wirelessredstone.registry;


import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.LinkerItem;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.item.SnifferItem;

public final class ModItems
{
	public static Item circuit;
	public static Item frequencyTool;
	public static Item frequencySniffer;
	public static Item remote;
	public static Item linker;

	public static void register()
	{
		// Block Item
		registerBlockItem(ModBlocks.redstoneTransmitter);
		registerBlockItem(ModBlocks.redstoneReceiver);
		registerBlockItem(ModBlocks.p2pRedstoneTransmitter);
		registerBlockItem(ModBlocks.p2pRedstoneReceiver);

		registerBlockItem(ModBlocks.redstoneTransmitterAttachment);
		registerBlockItem(ModBlocks.redstoneReceiverAttachment);
		registerBlockItem(ModBlocks.p2pRedstoneTransmitterAttachment);
		registerBlockItem(ModBlocks.p2pRedstoneReceiverAttachment);

		// Items
		circuit = registerItem("circuit", new Item(new Properties()));
		frequencyTool = registerItem("frequency_tool", new FrequencyItem(new Properties()));
		frequencySniffer = registerItem("frequency_sniffer", new SnifferItem(new Properties()));
		remote = registerItem("remote", new RemoteItem(new Properties()));
		linker = registerItem("linker", new LinkerItem(new Properties()));
	}

	private static Item registerItem(String name, Item item)
	{
		var resourceLocation = new ResourceLocation(WirelessRedstone.MOD_ID, name);
		Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
		return item;
	}

	private static void registerBlockItem(Block block)
	{
		var item = new BlockItem(block, new Properties());
		var resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
		Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
	}
}
