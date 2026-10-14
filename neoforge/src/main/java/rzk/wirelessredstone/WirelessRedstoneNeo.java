package rzk.wirelessredstone;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import rzk.wirelessredstone.client.WirelessRedstoneClientNeo;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.config.WRConfig;
import rzk.wirelessredstone.misc.WREvents;
import rzk.wirelessredstone.network.ModNetworking;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModComponents;
import rzk.wirelessredstone.registry.ModItems;

@Mod(WirelessRedstone.MOD_ID)
public class WirelessRedstoneNeo
{
	public WirelessRedstoneNeo(IEventBus modEventBus, ModContainer container)
	{
		modEventBus.addListener(this::registerEvent);
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(WirelessRedstoneClientNeo::clientSetup);
		modEventBus.addListener(WirelessRedstoneClientNeo::onRegisterRenderers);

		NeoForge.EVENT_BUS.register(WREvents.class);
		modEventBus.register(ModNetworking.class);

		if (WirelessRedstone.PLATFORM.isModLoaded("cloth_config")) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ModScreens::getConfigScreen);
		}
	}

	private void registerEvent(RegisterEvent event)
	{
		event.register(Registries.BLOCK, helper -> ModBlocks.register());
		event.register(Registries.BLOCK_ENTITY_TYPE, helper -> ModBlockEntities.register());
		event.register(Registries.DATA_COMPONENT_TYPE, helper -> ModComponents.register());
		event.register(Registries.ITEM, helper -> ModItems.register());
		event.register(Registries.CREATIVE_MODE_TAB, helper -> WirelessRedstone.registerCreativeTab());
	}

	private void loadComplete(FMLLoadCompleteEvent event)
	{
		WRConfig.load();
	}
}
