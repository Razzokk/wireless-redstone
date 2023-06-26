package rzk.wirelessredstone.item;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.datagen.LanguageBase;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.network.FrequencyItemPacket;

import java.util.List;

public class FrequencyItem extends Item
{
	public FrequencyItem(Settings settings)
	{
		super(settings);
	}

	public int getFrequency(ItemStack stack)
	{
		return WRUtils.readFrequency(stack.getNbt());
	}

	public void setFrequency(ItemStack stack, int frequency)
	{
		WRUtils.writeFrequency(stack.getOrCreateNbt(), frequency);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();

		if (world.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock transceiver)
		{
			PlayerEntity player = context.getPlayer();
			ItemStack stack = context.getStack();
			boolean isShift = player.isSneaking();

			int frequency = isShift ? transceiver.getFrequency(world, pos) : getFrequency(stack);

			if (!WRUtils.isValidFrequency(frequency))
				return ActionResult.FAIL;

			if (isShift)
				setFrequency(stack, frequency);
			else
				transceiver.setFrequency(world, pos, frequency);

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);

		if (!player.isSneaking())
			return TypedActionResult.pass(stack);

		if (!world.isClient)
			ServerPlayNetworking.send((ServerPlayerEntity) player, new FrequencyItemPacket(getFrequency(stack), hand));

		return TypedActionResult.success(stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		int frequency = getFrequency(stack);
		if (!WRUtils.isValidFrequency(frequency)) return;

		MutableText frequencyComponent = Text.literal(String.valueOf(frequency)).formatted(Formatting.AQUA);
		tooltip.add(Text.translatable(LanguageBase.ITEM_TOOLTIP_FREQUENCY, frequencyComponent).formatted(Formatting.GRAY));
	}
}