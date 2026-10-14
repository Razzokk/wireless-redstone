package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.WirelessRedstone;

public record FrequencyItemPacket(int frequency, InteractionHand hand) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<FrequencyItemPacket> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(WirelessRedstone.MOD_ID, "frequency_item"));

	public static final StreamCodec<FriendlyByteBuf, FrequencyItemPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, FrequencyItemPacket::frequency,
		ByteBufCodecs.BOOL, packet -> packet.hand == InteractionHand.MAIN_HAND,
		(frequency, hand) -> new FrequencyItemPacket(frequency, hand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND)
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
