package rzk.wirelessredstone.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.api.RedstoneConnectable;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.item.LinkerItem;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModItems;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public abstract class P2pRedstoneTransceiverBlock extends Block implements EntityBlock, RedstoneConnectable
{
	public P2pRedstoneTransceiverBlock()
	{
		super(Properties.of()
			.mapColor(MapColor.COLOR_LIGHT_GRAY)
			.isRedstoneConductor((state, blockGetter, pos) -> false)
			.strength(1.5F, 5.0F)
			.sound(SoundType.METAL));

		registerDefaultState(stateDefinition.any()
			.setValue(LINKED, false)
			.setValue(POWERED, false));
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
	{
		return state.getValue(LINKED) ? Redstone.SIGNAL_MAX : 0;
	}

	protected abstract boolean canLink(BlockState targetState, Level level, Player player);

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if (player.isShiftKeyDown()) return InteractionResult.PASS;

		var stack = player.getItemInHand(hand);
		if (!stack.is(ModItems.linker)) return InteractionResult.PASS;

		var target = LinkerItem.getTarget(stack);
		if (target == null) return InteractionResult.PASS;
		if (target == pos) return InteractionResult.FAIL;

		if (!level.isLoaded(target) && !level.isClientSide)
		{
			var targetText = WRUtils.positionText(target);
			WRUtils.appendTeleportCommandIfAllowed(targetText, player, target);
			var text = Component.translatable(TranslationKeys.MESSAGE_P2P_TARGET_UNLOADED, targetText).withStyle(ChatFormatting.RED);
			player.sendSystemMessage(text);
			return InteractionResult.FAIL;
		}

		var targetState = level.getBlockState(target);
		if (!canLink(targetState, level, player)) return InteractionResult.FAIL;

		var blockEntity = level.getBlockEntity(pos);
		if (!(blockEntity instanceof P2pRedstoneTransceiverBlockEntity p2pEntity)) return InteractionResult.PASS;

		var success = p2pEntity.link(target, player);
		return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(LINKED, POWERED);
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction)
	{
		return direction != null;
	}
}
