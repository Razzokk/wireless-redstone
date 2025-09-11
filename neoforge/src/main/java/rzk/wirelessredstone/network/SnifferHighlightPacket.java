package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.WRClientEventsNeo;
import rzk.wirelessredstone.misc.TranslationKeys;

public record SnifferHighlightPacket(long timestamp, InteractionHand hand, BlockPos[] coords) implements CustomPacketPayload
{
	public static final ResourceLocation ID = new ResourceLocation(WirelessRedstone.MOD_ID, "sniffer_highlight");

	public SnifferHighlightPacket(FriendlyByteBuf buf)
	{
		this(buf.readLong(), buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, readCoords(buf));
	}

	private static BlockPos[] readCoords(FriendlyByteBuf buf)
	{
		var coords = new BlockPos[buf.readInt()];
		for (int i = 0; i < coords.length; i++) coords[i] = buf.readBlockPos();
		return coords;
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeLong(timestamp);
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
		buf.writeInt(coords.length);

		for (BlockPos pos : coords)
			buf.writeBlockPos(pos);
	}

	@Override
	public ResourceLocation id()
	{
		return ID;
	}

	public void handle(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			if (FMLEnvironment.dist == Dist.CLIENT)
				WRClientEventsNeo.handleSnifferHighlightPacket(timestamp, hand, coords);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Component.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}
}
