package rzk.wirelessredstone.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.item.RemoteItem;

public class WREvents
{
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		PlayerEntity player = event.getEntity();
		World world = player.getWorld();
		ItemStack stack = player.getActiveItem();
		if (world.isClient || !stack.isOf(ModItems.remote)) return;
		((RemoteItem) stack.getItem()).onDeactivation(stack, world, player);
	}

	public static <T> T register(RegisterEvent.RegisterHelper<T> helper, String name, T object)
	{
		helper.register(WirelessRedstone.identifier(name), object);
		return object;
	}
}
