package rzk.wirelessredstone.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModComponents;

import java.util.List;

public class LinkerItem extends Item
{
	public LinkerItem(Properties properties)
	{
		super(properties.stacksTo(1));
	}

	private static void setTarget(ItemStack stack, BlockPos pos) {
		stack.set(ModComponents.target, pos);
	}

	public static BlockPos getTarget(ItemStack stack) {
		return stack.get(ModComponents.target);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		var player = context.getPlayer();
		if (!player.isShiftKeyDown()) return super.useOn(context);

		if (!context.getLevel().isClientSide)
			setTarget(context.getItemInHand(), context.getClickedPos());

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		var target = getTarget(stack);
		if (target == null) return;
		tooltip.add(WRUtils.targetText(target));
	}
}
