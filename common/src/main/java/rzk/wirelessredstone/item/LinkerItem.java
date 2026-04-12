package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.misc.NbtKeys;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.List;

public class LinkerItem extends Item
{
	public LinkerItem(Properties properties)
	{
		super(properties.stacksTo(1));
	}

	private static void setTarget(ItemStack stack, BlockPos pos)
	{
		var tag = WRUtils.writeBlockPos(pos);
		stack.addTagElement(NbtKeys.LINKER_TARGET, tag);
	}

	public static BlockPos getTarget(ItemStack stack)
	{
		var tag = stack.getTag();
		if (tag == null) return null;
		return WRUtils.readBlockPos(tag.get(NbtKeys.LINKER_TARGET));
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced)
	{
		var target = getTarget(stack);
		if (target == null) return;

		var targetText = WRUtils.positionText(target);
		tooltip.add(Component.translatable(TranslationKeys.TOOLTIP_TARGET, targetText).withStyle(ChatFormatting.GRAY));
	}
}
