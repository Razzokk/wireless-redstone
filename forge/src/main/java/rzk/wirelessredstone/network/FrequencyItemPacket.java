package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;

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

	public void handle(CustomPayloadEvent.Context ctx)
	{
		if (ctx.isClientSide()) handleClient(ctx);
		else handleServer(ctx);
	}

	private void handleServer(CustomPayloadEvent.Context ctx)
	{
		var player = ctx.getSender();
		var stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof FrequencyItem item)
			item.setFrequency(stack, frequency);
	}

	private void handleClient(CustomPayloadEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openItemFrequencyScreen(frequency, hand));
	}
}
