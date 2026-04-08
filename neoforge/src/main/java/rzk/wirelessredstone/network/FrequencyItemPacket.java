package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.TranslationKeys;

public record FrequencyItemPacket(int frequency, InteractionHand hand) implements CustomPacketPayload
{
	public static final ResourceLocation ID = new ResourceLocation(WirelessRedstone.MOD_ID, "frequency_item");

	public FrequencyItemPacket(FriendlyByteBuf buf)
	{
		this(buf.readInt(), buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}

	public void handleServer(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			var player = ctx.player().orElseThrow();
			var stack = player.getItemInHand(hand);
			if (stack.getItem() instanceof FrequencyItem)
				Frequency.set(stack, frequency);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Component.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}

	public void handleClient(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			if (FMLEnvironment.dist == Dist.CLIENT)
				ModScreens.openItemFrequencyScreen(frequency, hand);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Component.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}
}
