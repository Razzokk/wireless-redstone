package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import rzk.wirelessredstone.WirelessRedstone;

public record FrequencyBlockPacket(int frequency, BlockPos pos) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<FrequencyBlockPacket> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(WirelessRedstone.MOD_ID, "frequency_block"));

	public static final StreamCodec<FriendlyByteBuf, FrequencyBlockPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, FrequencyBlockPacket::frequency,
		BlockPos.STREAM_CODEC, FrequencyBlockPacket::pos,
		FrequencyBlockPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
