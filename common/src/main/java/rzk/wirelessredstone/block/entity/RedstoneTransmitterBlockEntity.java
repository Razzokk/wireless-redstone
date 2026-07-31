package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.registry.ModBlockEntities;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneTransmitterBlockEntity extends RedstoneTransceiverBlockEntity
{
	public RedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.redstoneTransmitterBlockEntityType, pos, state);
	}

	@Override
	protected void onFrequencyChange(int oldFrequency, int newFrequency)
	{
		if (level.isClientSide || !getBlockState().getValue(POWERED)) return;

		var ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.removeTransmitter(level, worldPosition, oldFrequency);

		if (Frequency.isValid(newFrequency))
			ether.addTransmitter(level, worldPosition, newFrequency);
	}

	public void onBlockPlaced(BlockState state, Level level, BlockPos pos)
	{
		if (level.isClientSide || !state.getValue(POWERED) || !Frequency.isValid(frequency)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.addTransmitter(level, pos, frequency);
	}

	public void onBlockRemoved(BlockState state, Level level, BlockPos pos)
	{
		if (level.isClientSide || !state.getValue(POWERED) || !Frequency.isValid(frequency)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.removeTransmitter(level, pos, frequency);
	}
}
