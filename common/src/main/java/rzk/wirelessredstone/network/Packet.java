package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rzk.wirelessredstone.WirelessRedstone;

import java.util.function.Function;

public interface Packet {
	void write(FriendlyByteBuf buf);

	Type<? extends Packet> type();

	static <T extends Packet> Type<T> createType(String id, Function<FriendlyByteBuf, T> creator) {
		return new Type<>(new ResourceLocation(WirelessRedstone.MOD_ID, id), creator);
	}

	record Type<T extends Packet>(ResourceLocation id, Function<FriendlyByteBuf, T> creator) {}
}
