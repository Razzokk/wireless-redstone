package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.ModScreens;

public record FrequencyBlockPacket(int frequency, BlockPos pos)
{
	public FrequencyBlockPacket(FriendlyByteBuf buf)
	{
		this(buf.readInt(), buf.readBlockPos());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBlockPos(pos);
	}

	public void handle(CustomPayloadEvent.Context ctx)
	{
		if (ctx.isClientSide()) handleClient(ctx);
		else handleServer(ctx);
	}

	private void handleServer(CustomPayloadEvent.Context ctx)
	{
		var level = ctx.getSender().level();
		if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
			block.setFrequency(level, pos, frequency);
	}

	private void handleClient(CustomPayloadEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openBlockFrequencyScreen(frequency, pos));
	}
}
