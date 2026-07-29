package rzk.wirelessredstone.misc;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.registry.ModItems;

public class WREvents
{
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		var player = event.getEntity();
		var level = player.level();
		var stack = player.getUseItem();
		if (level.isClientSide || !stack.is(ModItems.remote)) return;
		((RemoteItem) stack.getItem()).onDeactivation(stack, level, player);
	}
}
