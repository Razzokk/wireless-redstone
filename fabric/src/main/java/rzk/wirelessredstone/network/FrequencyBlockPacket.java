package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rzk.wirelessredstone.WirelessRedstone;

public record FrequencyBlockPacket(int frequency, BlockPos pos) implements FabricPacket
{
	public static final PacketType<FrequencyBlockPacket> TYPE = PacketType.create(
		new ResourceLocation(WirelessRedstone.MOD_ID, "frequency_block"),
		FrequencyBlockPacket::new);

	public FrequencyBlockPacket(FriendlyByteBuf buf)
	{
		this(buf.readInt(), buf.readBlockPos());
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBlockPos(pos);
	}

	@Override
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
