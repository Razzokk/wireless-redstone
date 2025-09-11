package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.api.ChunkLoadListener;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public abstract class P2pRedstoneTransceiverBlockEntity extends BlockEntity implements ChunkLoadListener
{
	protected BlockPos link;

	public P2pRedstoneTransceiverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public boolean link(BlockPos link, Player player)
	{
		if (link == null)
		{
			WirelessRedstone.LOGGER.error("Cannot link a null block position");
			return false;
		}

		if (link.equals(this.link))
		{
			var targetText = WRUtils.positionText(worldPosition);
			WRUtils.appendTeleportCommandIfAllowed(targetText, player, this.link);
			var text = Component.translatable(TranslationKeys.MESSAGE_P2P_ALREADY_LINKED, targetText);
			player.sendSystemMessage(text);
			return false;
		}

		if (!level.isLoaded(link))
		{
			WirelessRedstone.LOGGER.error("Block position should be loaded");
			return false;
		}

		if (this.link != null && !level.isClientSide) unlink();

		if (level.getBlockEntity(link) instanceof P2pRedstoneTransceiverBlockEntity other)
		{
			if (level.isClientSide) return true;
			if (other.link != null) other.unlink();

			onLinked(link);
			other.onLinked(worldPosition);
			var targetText = WRUtils.positionText(link);
			WRUtils.appendTeleportCommandIfAllowed(targetText, player, link);
			var text = Component.translatable(TranslationKeys.MESSAGE_P2P_LINKED, targetText);
			player.sendSystemMessage(text);

			return true;
		}

		WirelessRedstone.LOGGER.error("Target is not a transceiver block");
		return false;
	}

	public void unlink()
	{
		unlinkOther();
		onUnlinked();
	}

	public void unlinkOther()
	{
		if (level.isClientSide || link == null || !level.isLoaded(link)) return;

		if (level.getBlockEntity(link) instanceof P2pRedstoneTransceiverBlockEntity other)
			other.onUnlinked();
	}

	protected abstract void onLinked(BlockPos link);

	protected abstract void onUnlinked();

	private void virtualUnlink()
	{
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(LINKED, false));
	}

	private void virtualLink()
	{
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(LINKED, true));
	}

	@Override
	public void onChunkLoad(ServerLevel level)
	{
		if (level.isClientSide || link == null) return;

		if (!level.isLoaded(link))
		{
			virtualUnlink();
		}
		else if (level.getBlockEntity(link) instanceof P2pRedstoneTransceiverBlockEntity other && worldPosition.equals(other.link))
		{
			virtualLink();
			other.virtualLink();
		}
		else
		{
			unlink();
		}
	}

	@Override
	public void onChunkUnload(ServerLevel level)
	{
		if (level.isClientSide || link == null || !level.isLoaded(link)) return;

		if (level.getBlockEntity(link) instanceof P2pRedstoneTransceiverBlockEntity other)
			other.virtualUnlink();
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
		if (link == null) return;
		tag.put("link", NbtUtils.writeBlockPos(link));
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		if (!tag.contains("link")) return;
		link = NbtUtils.readBlockPos(tag.getCompound("link"));
	}
}
