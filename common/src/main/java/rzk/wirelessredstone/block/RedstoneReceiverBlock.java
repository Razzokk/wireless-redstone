package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.WRConfig;

import static net.minecraft.world.level.SignalGetter.DIRECTIONS;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneReceiverBlock extends RedstoneTransceiverBlock {
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (oldState.getBlock() == state.getBlock() || level.isClientSide) return;
		level.scheduleTick(pos, this, 0);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		super.onRemove(state, level, pos, newState, movedByPiston);
		if (level.isClientSide || !WRConfig.redstoneReceiverStrongPower.value) return;

		for (Direction direction : DIRECTIONS)
			level.updateNeighborsAtExceptFromFacing(pos.relative(direction), this, direction.getOpposite());
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		var ether = RedstoneEther.get(level);
		var frequency = getFrequency(level, pos);
		if (ether == null || !Frequency.isValid(frequency)) return;

		var powered = ether.isFrequencyActive(frequency);

		if (state.getValue(POWERED) != powered)
			level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_ALL);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) ? WRConfig.redstoneReceiverSignalStrength.value : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return WRConfig.redstoneReceiverStrongPower.value ? getSignal(state, level, pos, direction) : 0;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneReceiverBlockEntity(pos, state);
	}
}
