package rzk.wirelessredstone.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.SignalGetter.DIRECTIONS;

public interface RedstoneConnectable
{
	/**
	 * For the same purpose as {@link net.minecraft.world.level.block.state.BlockBehaviour#isSignalSource(BlockState)}
	 * but differentiating sides. Needs to match signature from neoforge/forge {@code IBlockExtension.canConnectRedstone}
	 */
	boolean canConnectRedstone(BlockState state, BlockGetter blockGetter, BlockPos pos, @Nullable Direction direction);

	/**
	 * Determines if the block is receiving redstone power on the given side/direction
	 */
	default boolean isPoweredOnSide(BlockState state, LevelAccessor levelAccessor, BlockPos pos, Direction direction)
	{
		if (!canConnectRedstone(state, levelAccessor, pos, direction)) return false;
		var accessDirection = direction.getOpposite();
		return levelAccessor.hasSignal(pos.relative(direction), accessDirection);
	}

	/**
	 * Determines if the block is receiving redstone power
	 */
	default boolean isReceivingRedstonePower(BlockState state, LevelAccessor levelAccessor, BlockPos pos)
	{
		for (Direction side : DIRECTIONS)
			if (isPoweredOnSide(state, levelAccessor, pos, side)) return true;
		return false;
	}
}
