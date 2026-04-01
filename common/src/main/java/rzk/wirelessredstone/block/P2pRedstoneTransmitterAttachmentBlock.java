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
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class P2pRedstoneTransmitterAttachmentBlock extends P2pRedstoneTransmitterBlock
{
	public P2pRedstoneTransmitterAttachmentBlock()
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
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		var world = ctx.getLevel();
		var pos = ctx.getClickedPos();
		var state = Attachment.getStateForPlacement(defaultBlockState(), ctx);
		var powered = isReceivingRedstonePower(state, world, pos);
		return state.setValue(POWERED, powered);
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
