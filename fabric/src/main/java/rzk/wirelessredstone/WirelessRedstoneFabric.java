package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import rzk.wirelessredstone.config.WRConfig;
import rzk.wirelessredstone.network.ModNetworking;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.RegisterUtil;

public class WirelessRedstoneFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		WRConfig.load();
		ModBlocks.register(registerUtil(Registry.BLOCK));
		ModBlockEntities.register(registerUtil(Registry.BLOCK_ENTITY_TYPE));
		ModItems.register(registerUtil(Registry.ITEM));
		ModNetworking.register();
	}

	private static <T> RegisterUtil<T> registerUtil(Registry<T> registry) {
		return (location, obj) -> Registry.register(registry, location, obj);
	}

	static {
		WirelessRedstone.CREATIVE_MODE_TAB = FabricItemGroupBuilder.create(
				new ResourceLocation(WirelessRedstone.MOD_ID, WirelessRedstone.MOD_ID)
			)
			.appendItems(items -> items.addAll(WirelessRedstone.getCreativeModTabItems()))
			.icon(() -> ModBlocks.redstoneTransmitter.asItem().getDefaultInstance())
			.build();
	}
}
