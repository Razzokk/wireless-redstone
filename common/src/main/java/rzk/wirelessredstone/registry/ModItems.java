package rzk.wirelessredstone.registry;


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
import rzk.wirelessredstone.misc.RegisterUtil;

public final class ModItems
{
	public static Item circuit;
	public static Item frequencyTool;
	public static Item frequencySniffer;
	public static Item remote;
	public static Item linker;

	public static void register(RegisterUtil<Item> util)
	{
		// Block Item
		registerBlockItem(util, ModBlocks.redstoneTransmitter);
		registerBlockItem(util, ModBlocks.redstoneReceiver);
		registerBlockItem(util, ModBlocks.p2pRedstoneTransmitter);
		registerBlockItem(util, ModBlocks.p2pRedstoneReceiver);

		// Items
		circuit = registerItem(util, "circuit", new Item(new Properties()));
		frequencyTool = registerItem(util, "frequency_tool", new FrequencyItem(new Properties()));
		frequencySniffer = registerItem(util, "frequency_sniffer", new SnifferItem(new Properties()));
		remote = registerItem(util, "remote", new RemoteItem(new Properties()));
		linker = registerItem(util, "linker", new LinkerItem(new Properties()));
	}

	private static Item registerItem(RegisterUtil<Item> util, String name, Item item)
	{
		var resourceLocation = new ResourceLocation(WirelessRedstone.MOD_ID, name);
		util.register(resourceLocation, item);
		return item;
	}

	private static void registerBlockItem(RegisterUtil<Item> util, Block block)
	{
		var item = new BlockItem(block, new Properties());
		var resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
		util.register(resourceLocation, item);
	}
}
