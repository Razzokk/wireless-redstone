package rzk.wirelessredstone.registries;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.items.LinkerItem;

import java.util.function.Supplier;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WirelessRedstone.MODID);

	public static final RegistryObject<Item> LINKER = registerItem("linker", () -> new LinkerItem(defaultItemProps()));

	public static RegistryObject<Item> registerItem(String name, Supplier<Item> supplier)
	{
		return ITEMS.register(name, supplier);
	}

	public static Item.Properties defaultItemProps()
	{
		return new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS);
	}
}