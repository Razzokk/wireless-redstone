package rzk.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;

import static net.minecraft.state.property.Properties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public class P2pRedstoneTransmitterBlockEntity extends P2pRedstoneTransceiverBlockEntity
{
	public P2pRedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.p2pRedstoneTransmitterBlockEntityType, pos, state);
	}

	public void scheduleReceiverUpdate()
	{
		if (world.isClient || link == null || !world.isChunkLoaded(link)) return;
		world.scheduleBlockTick(pos, ModBlocks.p2pRedstoneTransmitter, WRUtils.TICKS_PER_REDSTONE_TICK);
	}

	public void updateReceiver()
	{
		if (world.isClient || link == null || !world.isChunkLoaded(link)) return;
		var state = world.getBlockState(link);

		if (!state.isOf(ModBlocks.p2pRedstoneReceiver))
		{
			unlink();
			return;
		}

		var powered = getCachedState().get(POWERED);
		world.setBlockState(link, state.with(POWERED, powered));
	}

	@Override
	protected void onLinked(BlockPos link)
	{
		this.link = link;
		markDirty();
		world.setBlockState(pos, getCachedState().with(LINKED, true));
	}

	@Override
	protected void onUnlinked()
	{
		link = null;
		markDirty();
		world.setBlockState(pos, getCachedState().with(LINKED, false));
	}
}
