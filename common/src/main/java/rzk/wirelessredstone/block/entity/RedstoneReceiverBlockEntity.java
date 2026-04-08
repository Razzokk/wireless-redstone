package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.api.ChunkLoadListener;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.registry.ModBlockEntities;

public class RedstoneReceiverBlockEntity extends RedstoneTransceiverBlockEntity implements ChunkLoadListener
{
	public RedstoneReceiverBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.redstoneReceiverBlockEntityType, pos, state);
	}

	@Override
	protected void onFrequencyChange(int oldFrequency, int newFrequency)
	{
		if (level.isClientSide) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.removeReceiver(worldPosition, oldFrequency);

		if (Frequency.isValid(newFrequency))
			ether.addReceiver(level, worldPosition, newFrequency);
	}

	@Override
	public void onChunkLoad(ServerLevel level)
	{
		var ether = RedstoneEther.getOrCreate(level);
		ether.addReceiver(level, worldPosition, frequency);
	}

	@Override
	public void onChunkUnload(ServerLevel level)
	{
		var ether = RedstoneEther.get(level);
		if (ether == null) return;
		ether.removeReceiver(worldPosition, frequency);
	}
}
