package rzk.wirelessredstone.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface SelectedItemListener
{
	/**
	 * Needs to match the signature of neoforge/forge {@code IItemExtension.onDroppedByPlayer}
	 */
	boolean onDroppedByPlayer(ItemStack stack, Player player);

	/**
	 * Needs to match the signature of neoforge/forge {@code IItemExtension.onStopUsing}
	 */
	void onStopUsing(ItemStack stack, LivingEntity user, int count);
}
