package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.api.SelectedItemListener;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.TranslationKeys;

public class RemoteItem extends FrequencyItem implements SelectedItemListener
{
	public RemoteItem(Properties properties)
	{
		super(properties);
	}

	public void onDeactivation(ItemStack stack, Level level, LivingEntity owner)
	{
		if (!level.isClientSide)
		{
			var ether = RedstoneEther.get((ServerLevel) level);
			if (ether == null) return;
			int frequency = Frequency.get(stack);
			ether.removeRemote(level, owner, frequency);
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if (context.getPlayer().isShiftKeyDown()) return super.useOn(context);
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		if (player.isShiftKeyDown()) return super.use(level, player, usedHand);

		ItemStack stack = player.getItemInHand(usedHand);
		int frequency = Frequency.get(stack);
		player.getCooldowns().addCooldown(this, 10);

		if (!Frequency.isValid(frequency))
		{
			if (!level.isClientSide)
				player.displayClientMessage(Component.translatable(TranslationKeys.MESSAGE_NO_FREQUENCY).withStyle(ChatFormatting.RED), true);
			return InteractionResultHolder.consume(stack);
		}

		player.startUsingItem(usedHand);

		if (!level.isClientSide)
		{
			var ether = RedstoneEther.getOrCreate((ServerLevel) level);
			ether.addRemote(level, player, frequency);
		}

		return InteractionResultHolder.sidedSuccess(stack, false);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, Player player)
	{
		var level = player.level();
		if (level.isClientSide) return true;

		if (!player.getUseItem().isEmpty())
			onDeactivation(stack, level, player);

		return true;
	}

	@Override
	public void onStopUsing(ItemStack stack, LivingEntity user, int count)
	{
		var level = user.level();
		if (!level.isClientSide && !user.getUseItem().isEmpty())
			onDeactivation(stack, level, user);
	}
}
