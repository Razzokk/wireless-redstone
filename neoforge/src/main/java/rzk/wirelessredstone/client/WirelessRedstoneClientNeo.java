package rzk.wirelessredstone.client;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

public class WirelessRedstoneClientNeo
{
	public static void clientSetup(FMLClientSetupEvent event)
	{
		NeoForge.EVENT_BUS.register(WRClientEventsNeo.class);
		event.enqueueWork(WirelessRedstoneClient::registerItemProperties);
	}

	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		WirelessRedstoneClient.registerBlockEntityRenderers();
	}
}
