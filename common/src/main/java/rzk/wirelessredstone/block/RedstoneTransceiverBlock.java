package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.network.FrequencyBlockPacket;

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
		if (Frequency.isValid(frequency) && level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			transceiver.setFrequency(frequency);
	}

	public int getFrequency(Level level, BlockPos pos)
	{
		if (level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			return transceiver.getFrequency();
		return 0;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (stack.getItem() instanceof FrequencyItem)
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide) {
			var packet = new FrequencyBlockPacket(getFrequency(level, pos), pos);
			WirelessRedstone.PLATFORM.sendPacket((ServerPlayer) player, packet);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		int frequency = Frequency.get(stack);//TODO
		if (!Frequency.isValid(frequency)) return;
		tooltip.add(Frequency.tooltip(frequency));
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter blockGetter, BlockPos pos, @Nullable Direction direction)
	{
		return direction != null;
	}
}
