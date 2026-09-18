package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class RedstoneReceiverAttachmentBlock extends RedstoneReceiverBlock
{
	public RedstoneReceiverAttachmentBlock()
	{
		registerDefaultState(defaultBlockState()
			.setValue(ATTACH_FACE, AttachFace.FLOOR)
			.setValue(HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter blockGetter, BlockPos pos, @Nullable Direction direction)
	{
		return Attachment.canConnectRedstone(state, direction);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		if (!Attachment.canConnectRedstone(state, direction)) return 0;
		return super.getSignal(state, level, pos, direction);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		return Attachment.getStateForPlacement(defaultBlockState(), ctx);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
	{
		return Attachment.getShape(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(HORIZONTAL_FACING, ATTACH_FACE);
	}
}
