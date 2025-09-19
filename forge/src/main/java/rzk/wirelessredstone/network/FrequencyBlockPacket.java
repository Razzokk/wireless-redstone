package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.ModScreens;

import java.util.function.Supplier;

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

	public void handle(Supplier<NetworkEvent.Context> context)
	{
		var ctx = context.get();
		if (ctx.getDirection() == NetworkDirection.PLAY_TO_SERVER) handleServer(ctx);
		else handleClient(ctx);
	}

	private void handleServer(NetworkEvent.Context ctx)
	{
		var level = ctx.getSender().level();
		if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
			block.setFrequency(level, pos, frequency);
	}

	private void handleClient(NetworkEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openBlockFrequencyScreen(frequency, pos));
	}
}
