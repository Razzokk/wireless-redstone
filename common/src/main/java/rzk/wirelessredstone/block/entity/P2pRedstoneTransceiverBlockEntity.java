package rzk.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

public abstract class P2pRedstoneTransceiverBlockEntity extends BlockEntity
{
	protected BlockPos link;

	public P2pRedstoneTransceiverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public boolean link(BlockPos link, PlayerEntity player)
	{
		if (link == null)
		{
			WirelessRedstone.LOGGER.error("Cannot link a null block position");
			return false;
		}

		if (link.equals(this.link))
		{
			var targetText = WRUtils.positionText(pos);
			WRUtils.appendTeleportCommandIfAllowed(targetText, player, this.link);
			var text = Text.translatable(TranslationKeys.MESSAGE_P2P_ALREADY_LINKED, targetText);
			player.sendMessage(text);
			return false;
		}

		if (!world.isChunkLoaded(link))
		{
			WirelessRedstone.LOGGER.error("Block position should be loaded");
			return false;
		}

		if (this.link != null && !world.isClient) unlink();

		if (world.getBlockEntity(link) instanceof P2pRedstoneTransceiverBlockEntity other)
		{
			if (world.isClient) return true;
			if (other.link != null) other.unlink();

			onLinked(link);
			other.onLinked(pos);
			var targetText = WRUtils.positionText(link);
			WRUtils.appendTeleportCommandIfAllowed(targetText, player, link);
			var text = Text.translatable(TranslationKeys.MESSAGE_P2P_LINKED, targetText);
			player.sendMessage(text);

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
		if (world.isClient || link == null) return;

		if (world.isChunkLoaded(link) && world.getBlockEntity(link) instanceof P2pRedstoneTransceiverBlockEntity other)
			other.onUnlinked();
	}

	protected abstract void onLinked(BlockPos link);

	protected abstract void onUnlinked();

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		var linkedPosNbt = nbt.getCompound("link");
		if (linkedPosNbt != null) link = NbtHelper.toBlockPos(linkedPosNbt);
	}

	@Override
	protected void writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		if (link != null) nbt.put("link", NbtHelper.fromBlockPos(link));
	}
}
