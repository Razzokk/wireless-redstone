package rzk.wirelessredstone.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import rzk.wirelessredstone.client.platform.ClientPlatform;
import rzk.wirelessredstone.client.render.RedstoneTransceiverBER;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModItems;

public class WirelessRedstoneClient
{
	public static final ClientPlatform PLATFORM = ClientPlatform.load();

	public static void registerBlockEntityRenderers()
	{
		BlockEntityRenderers.register(ModBlockEntities.redstoneTransmitterBlockEntityType, RedstoneTransceiverBER::new);
		BlockEntityRenderers.register(ModBlockEntities.redstoneReceiverBlockEntityType, RedstoneTransceiverBER::new);
	}

	public static void registerItemProperties()
	{
		ItemProperties.register(ModItems.remote, new ResourceLocation("state"),
			(stack, world, entity, seed) -> ((entity != null && stack == entity.getUseItem()) ? 1f : 0f));
	}
}
