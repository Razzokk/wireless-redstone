package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.WirelessRedstone;

import java.nio.ByteBuffer;
import java.util.Arrays;

public record SnifferHighlightPacket(long timestamp, InteractionHand hand, BlockPos[] coords) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SnifferHighlightPacket> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(WirelessRedstone.MOD_ID, "sniffer_highlight"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SnifferHighlightPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_LONG, SnifferHighlightPacket::timestamp,
		ByteBufCodecs.BOOL, packet -> packet.hand == InteractionHand.MAIN_HAND,
		ByteBufCodecs.BYTE_ARRAY, packet -> convertCoords(packet.coords),
		(timestamp, hand, coordsBytes) -> new SnifferHighlightPacket(timestamp, hand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, readCoords(coordsBytes))
	);

	private static BlockPos[] readCoords(byte[] bytes) {
		if (bytes.length % Long.BYTES != 0) {
			WirelessRedstone.LOGGER.error("Sniffer packet has wrong number of bytes for block position array!");
			return new BlockPos[]{};
		}

		var numberOfCoords = bytes.length / Long.BYTES;
		var coords = new BlockPos[numberOfCoords];
		var buffer = ByteBuffer.wrap(bytes);

		for (int i = 0; i < numberOfCoords; i++) {
			coords[i] = BlockPos.of(buffer.getLong());
		}

		return coords;
	}

	private static byte[] convertCoords(BlockPos[] coords) {
		var longArray = Arrays.stream(coords).mapToLong(BlockPos::asLong).toArray();
		var buffer = ByteBuffer.allocate(longArray.length * Long.BYTES);
		buffer.asLongBuffer().put(longArray);
		return buffer.array();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
