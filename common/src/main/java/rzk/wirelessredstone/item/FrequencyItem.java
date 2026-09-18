package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.network.FrequencyItemPacket;

import java.util.List;

public class FrequencyItem extends Item
{
	public FrequencyItem(Properties properties)
	{
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		var level = context.getLevel();
		var pos = context.getClickedPos();

		if (level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock transceiver)
		{
			var player = context.getPlayer();
			var stack = context.getItemInHand();
			var isShift = player.isShiftKeyDown();

			int frequency = isShift ? transceiver.getFrequency(level, pos) : Frequency.get(stack);

			if (!Frequency.isValid(frequency)) {
				if (level.isClientSide)
					player.displayClientMessage(Component.translatable(TranslationKeys.MESSAGE_NO_FREQUENCY).withStyle(ChatFormatting.RED), true);
				return InteractionResult.FAIL;
			}

			if (isShift) {
				Frequency.set(stack, frequency);

				if (level.isClientSide)
					player.displayClientMessage(Component.translatable(TranslationKeys.MESSAGE_FREQUENCY_COPIED, Frequency.text(frequency)), true);
			}
			else {
				transceiver.setFrequency(level, pos, frequency);

				if (level.isClientSide)
					player.displayClientMessage(Component.translatable(TranslationKeys.MESSAGE_FREQUENCY_SET, Frequency.text(frequency)), true);
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		ItemStack stack = player.getItemInHand(usedHand);

		if (!player.isShiftKeyDown())
			return InteractionResultHolder.pass(stack);

		if (!level.isClientSide) {
			var packet = new FrequencyItemPacket(Frequency.get(stack), usedHand);
			WirelessRedstone.PLATFORM.sendPacket((ServerPlayer) player, packet);
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced)
	{
		int frequency = Frequency.get(stack);
		if (!Frequency.isValid(frequency)) return;
		tooltip.add(Frequency.tooltip(frequency));
	}
}
