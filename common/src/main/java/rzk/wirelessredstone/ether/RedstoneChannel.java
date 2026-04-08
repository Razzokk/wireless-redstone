package rzk.wirelessredstone.ether;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.block.RedstoneReceiverBlock;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.HashSet;
import java.util.Set;

public class RedstoneChannel
{
	private final int frequency;
	// Contains only active transmitters
	private final Set<BlockPos> transmitters = new HashSet<>();
	// Contains only currently listening/loaded receivers
	private final Set<BlockPos> receivers = new HashSet<>();
	// Player must be holding and activating remote, thus only one remote per player can be active
	// Additionally, remotes do not need to be saved because if the player logs off the remote gets deactivated,
	// same holds for shutting down the server/world.
	private final Set<LivingEntity> remotes = new HashSet<>();

	public RedstoneChannel(int frequency)
	{
		this.frequency = frequency;
	}

	public RedstoneChannel(CompoundTag tag)
	{
		frequency = Frequency.get(tag);

		ListTag transmitterTags = tag.getList("transmitters", Tag.TAG_COMPOUND);
		for (var transmitterTag : transmitterTags)
			transmitters.add(NbtUtils.readBlockPos((CompoundTag) transmitterTag));
	}

	public CompoundTag save()
	{
		CompoundTag tag = new CompoundTag();
		Frequency.set(tag, frequency);

		ListTag transmitterTags = new ListTag();
		for (BlockPos pos : transmitters)
			transmitterTags.add(NbtUtils.writeBlockPos(pos));
		tag.put("transmitters", transmitterTags);

		return tag;
	}

	public void addTransmitter(Level level, BlockPos pos)
	{
		boolean empty = !isActive();
		transmitters.add(pos);
		if (empty) updateReceivers(level);
	}

	public void removeTransmitter(Level level, BlockPos pos)
	{
		transmitters.remove(pos);
		if (!isActive())
			updateReceivers(level);
	}

	public void addReceiver(Level level, BlockPos pos)
	{
		receivers.add(pos);
		updateReceiver(level, pos);
	}

	public void removeReceiver(BlockPos pos)
	{
		receivers.remove(pos);
	}

	public void addRemote(Level level, LivingEntity owner)
	{
		boolean empty = !isActive();
		remotes.add(owner);
		if (empty) updateReceivers(level);
	}

	public void removeRemote(Level level, LivingEntity owner)
	{
		remotes.remove(owner);
		if (!isActive())
			updateReceivers(level);
	}

	public void updateReceiver(Level level, BlockPos pos)
	{
		var block = level.getBlockState(pos).getBlock();
		if (!(block instanceof RedstoneReceiverBlock)) return;
		level.scheduleTick(pos, block, WRUtils.TICKS_PER_REDSTONE_TICK);
	}

	public void updateReceivers(Level level)
	{
		for (BlockPos receiver : receivers)
			updateReceiver(level, receiver);
	}

	public int getFrequency()
	{
		return frequency;
	}

	public Set<BlockPos> getTransmitters()
	{
		return transmitters;
	}

	public boolean isActive()
	{
		return !transmitters.isEmpty() || !remotes.isEmpty();
	}

	public boolean isEmpty()
	{
		return transmitters.isEmpty() && receivers.isEmpty() && remotes.isEmpty();
	}
}
