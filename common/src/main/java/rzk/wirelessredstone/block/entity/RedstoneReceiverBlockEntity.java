package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.registry.ModBlockEntities;

public class RedstoneReceiverBlockEntity extends RedstoneTransceiverBlockEntity {
	public RedstoneReceiverBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.redstoneReceiverBlockEntityType, pos, state);
	}

	@Override
	protected void onFrequencyChange(int oldFrequency, int newFrequency) {
		if (level.isClientSide) return;
		var ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.removeReceiver(worldPosition, oldFrequency);

		if (Frequency.isValid(newFrequency))
			ether.addReceiver(level, worldPosition, newFrequency);
	}

	public void onLoad() {
		if (!(level instanceof ServerLevel lvl) || !Frequency.isValid(frequency)) return;
		var ether = RedstoneEther.getOrCreate(lvl);
		ether.addReceiver(level, worldPosition, frequency);
	}

	public void onUnload() {
		if (!(level instanceof ServerLevel lvl) || !Frequency.isValid(frequency)) return;
		var ether = RedstoneEther.get(lvl);
		if (ether != null) ether.removeReceiver(worldPosition, frequency);
	}

	@Override
	public void clearRemoved() {
		super.clearRemoved();
		if (level == null || level.isClientSide) return;
		level.blockEvent(getBlockPos(), getBlockState().getBlock(), 0, 0);
	}

	@Override
	public void setRemoved() {
		onUnload();
		super.setRemoved();
	}
}
