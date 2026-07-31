package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.Frequency;

import java.util.function.Supplier;

public record FrequencyItemPacket(int frequency, InteractionHand hand)
{
	public FrequencyItemPacket(FriendlyByteBuf buf)
	{
		this(buf.readInt(), buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
	}

	public void handle(Supplier<NetworkEvent.Context> context)
	{
		var ctx = context.get();
		if (ctx.getDirection() == NetworkDirection.PLAY_TO_SERVER) handleServer(ctx);
		else handleClient(ctx);
	}

	private void handleServer(NetworkEvent.Context ctx)
	{
		var player = ctx.getSender();
		var stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof FrequencyItem)
			Frequency.set(stack, frequency);
	}

	private void handleClient(NetworkEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openItemFrequencyScreen(frequency, hand));
	}
}
