package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class Attachment {
	private static final VoxelShape FLOOR_SHAPE = Block.box(2, 0, 2, 14, 2, 14);
	private static final VoxelShape CEILING_SHAPE = Block.box(2, 14, 2, 14, 16, 14);
	private static final VoxelShape NORTH_SHAPE = Block.box(2, 2, 14, 14, 14, 16);
	private static final VoxelShape SOUTH_SHAPE = Block.box(2, 2, 0, 14, 14, 2);
	private static final VoxelShape WEST_SHAPE = Block.box(14, 2, 2, 16, 14, 14);
	private static final VoxelShape EAST_SHAPE = Block.box(0, 2, 2, 2, 14, 14);

	public static VoxelShape getShape(BlockState state)
	{
		return switch (state.getValue(ATTACH_FACE))
		{
			case FLOOR -> FLOOR_SHAPE;
			case CEILING -> CEILING_SHAPE;
			case WALL -> switch (state.getValue(HORIZONTAL_FACING))
			{
				case NORTH -> NORTH_SHAPE;
				case SOUTH -> SOUTH_SHAPE;
				case WEST -> WEST_SHAPE;
				case EAST -> EAST_SHAPE;
				default -> throw new IllegalStateException("Unexpected value: " + state.getValue(HORIZONTAL_FACING));
			};
		};
	}

	public static boolean canConnectRedstone(BlockState state, Direction direction) {
		return getFacing(state) == direction;
	}

	public static boolean hasSignal(BlockState state, Level level, BlockPos pos) {
		var facing = Attachment.getFacing(state);
		var direction = facing.getOpposite();
		return level.hasSignal(pos.relative(direction), direction);
	}

	public static Direction getFacing(BlockState state) {
		return switch (state.getValue(ATTACH_FACE))
		{
			case FLOOR -> Direction.UP;
			case CEILING -> Direction.DOWN;
			case WALL -> state.getValue(HORIZONTAL_FACING);
		};
	}

	public static BlockState getStateForPlacement(BlockState baseState, BlockPlaceContext ctx) {
		var face = switch (ctx.getClickedFace()) {
			case UP -> AttachFace.FLOOR;
			case DOWN -> AttachFace.CEILING;
			default -> AttachFace.WALL;
		};

		var facing = face == AttachFace.WALL ? ctx.getClickedFace() : ctx.getHorizontalDirection();

		return baseState
			.setValue(ATTACH_FACE, face)
			.setValue(HORIZONTAL_FACING, facing);
	}
}
