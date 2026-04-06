package rzk.wirelessredstone.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface RedstoneConnectable
{
	/**
	 * For the same purpose as {@link net.minecraft.world.level.block.state.BlockBehaviour#isSignalSource(BlockState)}
	 * but differentiating sides. Needs to match signature from neoforge/forge {@code IBlockExtension.canConnectRedstone}
	 */
	boolean canConnectRedstone(BlockState state, BlockGetter blockGetter, BlockPos pos, @Nullable Direction direction);
}
