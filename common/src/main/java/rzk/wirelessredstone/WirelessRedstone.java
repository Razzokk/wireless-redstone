package rzk.wirelessredstone;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.platform.Platform;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

import java.util.List;
import java.util.stream.Stream;

public final class WirelessRedstone {
	public static final String MOD_ID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Platform PLATFORM = Platform.load();

	public static CreativeModeTab CREATIVE_MODE_TAB;

	public static List<ItemStack> getCreativeModTabItems() {
		return Stream.of(
			ModBlocks.redstoneTransmitter,
			ModBlocks.redstoneReceiver,
			ModBlocks.p2pRedstoneTransmitter,
			ModBlocks.p2pRedstoneReceiver,
			ModBlocks.redstoneTransmitterAttachment,
			ModBlocks.redstoneReceiverAttachment,
			ModBlocks.p2pRedstoneTransmitterAttachment,
			ModBlocks.p2pRedstoneReceiverAttachment,
			ModItems.circuit,
			ModItems.frequencyTool,
			ModItems.frequencySniffer,
			ModItems.remote,
			ModItems.linker
		).map(itemLike -> itemLike.asItem().getDefaultInstance()).toList();
	}
}
