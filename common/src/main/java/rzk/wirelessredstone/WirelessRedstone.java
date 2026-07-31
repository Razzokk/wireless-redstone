package rzk.wirelessredstone;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.platform.Platform;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.RegisterUtil;

public final class WirelessRedstone {
	public static final String MOD_ID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Platform PLATFORM = Platform.load();

	private static final CreativeModeTab CREATIVE_MODE_TAB = CreativeModeTab.builder(null, 0)
		.displayItems((params, entries) -> {
			entries.accept(ModBlocks.redstoneTransmitter);
			entries.accept(ModBlocks.redstoneReceiver);
			entries.accept(ModBlocks.p2pRedstoneTransmitter);
			entries.accept(ModBlocks.p2pRedstoneReceiver);
			entries.accept(ModBlocks.redstoneTransmitterAttachment);
			entries.accept(ModBlocks.redstoneReceiverAttachment);
			entries.accept(ModBlocks.p2pRedstoneTransmitterAttachment);
			entries.accept(ModBlocks.p2pRedstoneReceiverAttachment);
			entries.accept(ModItems.circuit);
			entries.accept(ModItems.frequencyTool);
			entries.accept(ModItems.frequencySniffer);
			entries.accept(ModItems.remote);
			entries.accept(ModItems.linker);
		})
		.title(Component.translatable(TranslationKeys.ITEM_GROUP_WIRELESS_REDSTONE))
		.icon(() -> new ItemStack(ModBlocks.redstoneTransmitter))
		.build();

	public static void registerCreativeTab(RegisterUtil<CreativeModeTab> util) {
		util.register(new ResourceLocation(WirelessRedstone.MOD_ID, WirelessRedstone.MOD_ID), CREATIVE_MODE_TAB);
	}
}
