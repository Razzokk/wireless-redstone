package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import rzk.wirelessredstone.WirelessRedstone;

import java.util.function.Function;

public interface Packet extends CustomPacketPayload {
	void write(FriendlyByteBuf buf);

	Type<? extends Packet> type();

	@Override
	default ResourceLocation id() {
		return type().id;
	}

	static <T extends Packet> Type<T> createType(String id, FriendlyByteBuf.Reader<T> reader) {
		return new Type<>(new ResourceLocation(WirelessRedstone.MOD_ID, id), reader);
	}

	record Type<T extends Packet>(ResourceLocation id, FriendlyByteBuf.Reader<T> reader) {}
}
