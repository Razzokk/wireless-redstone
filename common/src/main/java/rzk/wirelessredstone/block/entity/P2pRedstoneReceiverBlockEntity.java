package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.registry.ModBlockEntities;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public class P2pRedstoneReceiverBlockEntity extends P2pRedstoneTransceiverBlockEntity
{
	public P2pRedstoneReceiverBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.p2pRedstoneReceiverBlockEntityType, pos, state);
	}

	@Override
	protected void onLinked(BlockPos link)
	{
		this.link = link;
		setChanged();
		var transmitterState = level.getBlockState(this.link);
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(LINKED, true).setValue(POWERED, transmitterState.getValue(POWERED)));
	}

	@Override
	protected void onUnlinked()
	{
		link = null;
		setChanged();
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(LINKED, false).setValue(POWERED, false));
	}

	@Override
	public void onChunkLoad(ServerLevel level)
	{
		super.onChunkLoad(level);
		if (level.isClientSide || link == null) return;

		var transmitterState = level.getBlockState(link);
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(POWERED, transmitterState.getValue(POWERED)));
	}
}
