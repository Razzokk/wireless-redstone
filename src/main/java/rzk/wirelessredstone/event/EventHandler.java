package rzk.wirelessredstone.event;

import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import rzk.lib.mc.util.TaskScheduler;

@Mod.EventBusSubscriber
public class EventHandler
{
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
			TaskScheduler.onWorldTick(event.world);
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event)
	{
		TaskScheduler.onWorldUnload((World) event.getWorld());
	}

	@SubscribeEvent
	public static void onServerStop(FMLServerStoppedEvent event)
	{
		TaskScheduler.onServerStop(event.getServer());
	}
}