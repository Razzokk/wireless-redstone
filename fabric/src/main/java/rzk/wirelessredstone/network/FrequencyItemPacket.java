package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.WirelessRedstone;

public record FrequencyItemPacket(int frequency, InteractionHand hand) implements FabricPacket
{
	public static final PacketType<FrequencyItemPacket> TYPE = PacketType.create(
		new ResourceLocation(WirelessRedstone.MOD_ID, "frequency_item"),
		FrequencyItemPacket::new);

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
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
