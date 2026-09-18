package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public record FrequencyBlockPacket(int frequency, BlockPos pos) implements Packet {
	public static final Type<FrequencyBlockPacket> TYPE = Packet.createType("frequency_block", FrequencyBlockPacket::new);

	public FrequencyBlockPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readBlockPos());
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(frequency);
		buf.writeBlockPos(pos);
	}

	@Override
	public Type<? extends Packet> type() {
		return TYPE;
	}
}
