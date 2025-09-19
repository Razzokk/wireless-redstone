package rzk.wirelessredstone.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.api.RedstoneConnectable;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public abstract class RedstoneTransceiverBlock extends Block implements EntityBlock, RedstoneConnectable
{
	public RedstoneTransceiverBlock()
	{
		super(Properties.of()
			.mapColor(MapColor.COLOR_LIGHT_GRAY)
			.isRedstoneConductor((state, blockGetter, pos) -> false)
			.strength(1.5F, 5.0F)
			.sound(SoundType.METAL));

		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	public void setFrequency(Level level, BlockPos pos, int frequency)
	{
		if (WRUtils.isValidFrequency(frequency) && level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			transceiver.setFrequency(frequency);
	}

	public int getFrequency(Level level, BlockPos pos)
	{
		if (level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			return transceiver.getFrequency();
		return 0;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		var item = player.getItemInHand(hand).getItem();

		if (item instanceof FrequencyItem)
			return InteractionResult.PASS;

		if (!level.isClientSide)
			WirelessRedstone.PLATFORM.sendFrequencyBlockPacket((ServerPlayer) player, getFrequency(level, pos), pos);

		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		var frequency = WRUtils.readFrequency(BlockItem.getBlockEntityData(stack));
		if (!WRUtils.isValidFrequency(frequency)) return;

		var frequencyComponent = Component.literal(String.valueOf(frequency)).withStyle(ChatFormatting.AQUA);
		tooltip.add(Component.translatable(TranslationKeys.TOOLTIP_FREQUENCY, frequencyComponent).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter blockGetter, BlockPos pos, @Nullable Direction direction)
	{
		return direction != null;
	}
}
