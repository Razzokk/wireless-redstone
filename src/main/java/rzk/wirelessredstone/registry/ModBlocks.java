package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.wirelessredstone.WirelessRedstone;

public final class ModBlocks
{
    public static final ObjectList<Block> BLOCKS = new ObjectArrayList<>();
    public static final ObjectList<Item> ITEMS = new ObjectArrayList<>();

    public static Block test;

    private ModBlocks() {}

    private static void initBlocks()
    {
        test = registerBlock("test_block", new Block(Material.IRON));
    }

    private static Block registerBlockNoItem(String name, Block block)
    {
        block.setCreativeTab(WirelessRedstone.CREATIVE_TAB)
                .setUnlocalizedName(WirelessRedstone.MOD_ID + '.' + name)
                .setRegistryName(WirelessRedstone.MOD_ID, name);
        BLOCKS.add(block);
        return block;
    }

    private static Block registerBlock(String name, Block block, ItemBlock item)
    {
        registerBlockNoItem(name, block);
        item.setRegistryName(block.getRegistryName());
        ITEMS.add(item);
        return block;
    }

    private static Block registerBlock(String name, Block block)
    {
        return registerBlock(name, block, new ItemBlock(block));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();
        initBlocks();
        BLOCKS.forEach(registry::register);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        ITEMS.forEach(registry::register);
    }
}
