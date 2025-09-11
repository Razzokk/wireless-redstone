package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.misc.WRUtils;

public abstract class RedstoneTransceiverBlockEntity extends BlockEntity
{
	protected int frequency = WRUtils.INVALID_FREQUENCY;

	public RedstoneTransceiverBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
	{
		super(blockEntityType, pos, state);
	}

	protected abstract void onFrequencyChange(int oldFrequency, int newFrequency);

	public int getFrequency()
	{
		return frequency;
	}

	public void setFrequency(int frequency)
	{
		if (frequency == this.frequency) return;
		onFrequencyChange(this.frequency, frequency);
		this.frequency = frequency;
		setChanged();
		var state = getBlockState();
		level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
	}

	@Override
	public @Nullable Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tag = new CompoundTag();
		WRUtils.writeFrequency(tag, frequency);
		return tag;
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		frequency = WRUtils.readFrequency(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
		WRUtils.writeFrequency(tag, frequency);
	}
}
