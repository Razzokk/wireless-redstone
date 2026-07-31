package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
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
		ModBlocks.register();
		ModItems.register();
		ModBlockEntities.register();
		ModNetworking.register();
		WirelessRedstone.registerCreativeTab();
	}
}
