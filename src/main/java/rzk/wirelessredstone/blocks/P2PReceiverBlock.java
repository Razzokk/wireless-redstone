package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class P2PReceiverBlock extends P2PTransceiverBlock
{
    public P2PReceiverBlock(Properties props)
    {
        super(props);
    }

    @Override
    public boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
    {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
    {
        return getSignal(state, blockGetter, pos, direction);
    }
}