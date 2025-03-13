package rzk.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.registry.ModBlockEntities;

import static net.minecraft.state.property.Properties.POWERED;
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
		markDirty();
		var transmitterState = world.getBlockState(this.link);
		world.setBlockState(pos, getCachedState().with(LINKED, true).with(POWERED, transmitterState.get(POWERED)));
	}

	@Override
	protected void onUnlinked()
	{
		link = null;
		markDirty();
		world.setBlockState(pos, getCachedState().with(LINKED, false).with(POWERED, false));
	}

	@Override
	public void onChunkLoad(ServerWorld world)
	{
		super.onChunkLoad(world);
		if (world.isClient || link == null) return;

		var transmitterState = world.getBlockState(link);
		world.setBlockState(pos, getCachedState().with(POWERED, transmitterState.get(POWERED)));
	}
}
