package rzk.wirelessredstone.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.P2pRedstoneReceiverBlockEntity;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;

import static net.minecraft.world.level.SignalGetter.DIRECTIONS;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class P2pRedstoneReceiverBlock extends P2pRedstoneTransceiverBlock
{
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{

		if (state.getBlock() != newState.getBlock())
		{
			level.getBlockEntity(pos, ModBlockEntities.p2pRedstoneReceiverBlockEntityType)
				.ifPresent(P2pRedstoneTransceiverBlockEntity::unlinkOther);
		}

		if (!level.isClientSide && WRConfig.redstoneReceiverStrongPower.value)
			for (Direction direction : DIRECTIONS)
				level.updateNeighborsAtExceptFromFacing(pos.relative(direction), this, direction.getOpposite());

		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return state.getValue(POWERED) ? WRConfig.redstoneReceiverSignalStrength.value : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return WRConfig.redstoneReceiverStrongPower.value ? getSignal(state, level, pos, direction) : 0;
	}

	@Override
	protected boolean canLink(BlockState targetState, Level level, Player player)
	{
		if (targetState.is(ModBlocks.p2pRedstoneTransmitter) || targetState.is(ModBlocks.p2pRedstoneTransmitterAttachment)) return true;

		if (!level.isClientSide)
		{
			var receiverTranslated = ModBlocks.p2pRedstoneTransmitter.getName().withStyle(ChatFormatting.AQUA);
			var text = Component.translatable(TranslationKeys.MESSAGE_P2P_WRONG_TARGET, receiverTranslated);
			player.sendSystemMessage(text);
		}

		return false;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new P2pRedstoneReceiverBlockEntity(pos, state);
	}
}
