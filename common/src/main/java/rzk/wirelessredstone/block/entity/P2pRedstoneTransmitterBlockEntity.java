package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public class P2pRedstoneTransmitterBlockEntity extends P2pRedstoneTransceiverBlockEntity
{
	public P2pRedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.p2pRedstoneTransmitterBlockEntityType, pos, state);
	}

	public void scheduleReceiverUpdate()
	{
		if (level.isClientSide || link == null || !level.isLoaded(link)) return;
		level.scheduleTick(worldPosition, ModBlocks.p2pRedstoneTransmitter, WRUtils.TICKS_PER_REDSTONE_TICK);
	}

	public void updateReceiver()
	{
		if (level.isClientSide || link == null || !level.isLoaded(link)) return;
		var state = level.getBlockState(link);

		if (!state.is(ModBlocks.p2pRedstoneReceiver))
		{
			unlink();
			return;
		}

		var powered = getBlockState().getValue(POWERED);
		level.setBlockAndUpdate(link, state.setValue(POWERED, powered));
	}

	@Override
	protected void onLinked(BlockPos link)
	{
		this.link = link;
		setChanged();
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(LINKED, true));
	}

	@Override
	protected void onUnlinked()
	{
		link = null;
		setChanged();
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(LINKED, false));
	}
}
