package rzk.wirelessredstone.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.render.TERFrequency;
import rzk.wirelessredstone.registry.ModTiles;

@Mod.EventBusSubscriber(modid = WirelessRedstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientSubscriber
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(ModTiles.frequency, TERFrequency::new);
	}
}