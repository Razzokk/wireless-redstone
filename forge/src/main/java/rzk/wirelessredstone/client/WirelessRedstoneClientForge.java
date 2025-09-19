package rzk.wirelessredstone.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class WirelessRedstoneClientForge
{
	public static void clientSetup(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(WRClientEventsForge.class);
		event.enqueueWork(WirelessRedstoneClient::registerItemProperties);
	}

	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		WirelessRedstoneClient.registerBlockEntityRenderers();
	}
}
