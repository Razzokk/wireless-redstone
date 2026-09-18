package rzk.wirelessredstone;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import rzk.wirelessredstone.client.WirelessRedstoneClientForge;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.config.WRConfig;
import rzk.wirelessredstone.misc.WREvents;
import rzk.wirelessredstone.network.ModNetworking;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

@Mod(WirelessRedstone.MOD_ID)
public class WirelessRedstoneForge {
	public WirelessRedstoneForge() {
		var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::registerEvent);
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(WirelessRedstoneClientForge::clientSetup);
		modEventBus.addListener(WirelessRedstoneClientForge::onRegisterRenderers);

		MinecraftForge.EVENT_BUS.register(WREvents.class);
	}

	private void registerEvent(RegisterEvent event) {
		event.register(Registry.BLOCK_REGISTRY, helper -> ModBlocks.register(helper::register));
		event.register(Registry.BLOCK_ENTITY_TYPE_REGISTRY, helper -> ModBlockEntities.register(helper::register));
		event.register(Registry.ITEM_REGISTRY, helper -> ModItems.register(helper::register));
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		ModNetworking.registerMessages();
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		WRConfig.load();

		if (WirelessRedstone.PLATFORM.isModLoaded("cloth_config")) {
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory(ModScreens::getConfigScreen));
		}
	}

	static {
		WirelessRedstone.CREATIVE_MODE_TAB = new CreativeModeTab(
			(new ResourceLocation(WirelessRedstone.MOD_ID, WirelessRedstone.MOD_ID)).toLanguageKey()
		) {
			@Override
			public ItemStack makeIcon() {
				return ModBlocks.redstoneTransmitter.asItem().getDefaultInstance();
			}

			@Override
			public void fillItemList(NonNullList<ItemStack> items) {
				items.addAll(WirelessRedstone.getCreativeModTabItems());
			}
		};
	}
}
