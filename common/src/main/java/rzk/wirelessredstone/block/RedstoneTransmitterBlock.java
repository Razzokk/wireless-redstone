package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.registry.ModBlockEntities;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneTransmitterBlock extends RedstoneTransceiverBlock {
	protected boolean hasSignal(BlockState state, Level level, BlockPos pos) {
		return level.hasNeighborSignal(pos);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (oldState.getBlock() != this) {
			level.scheduleTick(pos, this, 0);
			return;
		}

		level.getBlockEntity(pos, ModBlockEntities.redstoneTransmitterBlockEntityType)
			.ifPresent(entity -> entity.onBlockPlaced(state, level, pos));
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		var powered = hasSignal(state, level, pos);
		if (state.getValue(POWERED) != powered) {
			level.setBlock(pos, state.setValue(POWERED, powered), UPDATE_CLIENTS);
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		level.getBlockEntity(pos, ModBlockEntities.redstoneTransmitterBlockEntityType)
			.ifPresent(entity -> entity.onBlockRemoved(state, level, pos));
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		if (level.isClientSide) return;
		var powered = hasSignal(state, level, pos);
		if (state.getValue(POWERED) == powered) return;
		level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneTransmitterBlockEntity(pos, state);
	}
}
