package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import rzk.wirelessredstone.misc.RegisterUtil;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.network.ModNetworking;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public class WirelessRedstoneFabric implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		WRConfig.load();
		ModBlocks.register(registerUtil(BuiltInRegistries.BLOCK));
		ModBlockEntities.register(registerUtil(BuiltInRegistries.BLOCK_ENTITY_TYPE));
		ModItems.register(registerUtil(BuiltInRegistries.ITEM));
		ModNetworking.register();
		WirelessRedstone.registerCreativeTab(registerUtil(BuiltInRegistries.CREATIVE_MODE_TAB));

		ServerChunkEvents.CHUNK_LOAD.register(WirelessRedstone::onChunkLoad);
		ServerChunkEvents.CHUNK_UNLOAD.register(WirelessRedstone::onChunkUnload);
	}

	private static <T> RegisterUtil<T> registerUtil(Registry<T> registry)
	{
		return (location, obj) -> Registry.register(registry, location, obj);
	}
}
