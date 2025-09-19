package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.WirelessRedstone;

public record SnifferHighlightPacket(long timestamp, InteractionHand hand, BlockPos[] coords) implements FabricPacket
{
	public static final PacketType<SnifferHighlightPacket> TYPE = PacketType.create(
		new ResourceLocation(WirelessRedstone.MOD_ID, "sniffer_highlight"),
		SnifferHighlightPacket::new);

	public SnifferHighlightPacket(FriendlyByteBuf buf)
	{
		this(buf.readLong(), buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, readCoords(buf));
	}

	private static BlockPos[] readCoords(FriendlyByteBuf buf)
	{
		var coords = new BlockPos[buf.readInt()];
		for (int i = 0; i < coords.length; i++)
			coords[i] = buf.readBlockPos();
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
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
