package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public record FrequencyItemPacket(int frequency, InteractionHand hand) implements Packet {
	public static final Type<FrequencyItemPacket> TYPE = Packet.createType("frequency_item", FrequencyItemPacket::new);

	public FrequencyItemPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(frequency);
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
	}

	@Override
	public Type<? extends Packet> type() {
		return TYPE;
	}
}
