package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.misc.TranslationKeys;

public record FrequencyBlockPacket(int frequency, BlockPos pos) implements CustomPacketPayload
{
	public static final ResourceLocation ID = new ResourceLocation(WirelessRedstone.MOD_ID, "frequency_block");

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
	public ResourceLocation id()
	{
		return ID;
	}

	public void handleServer(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			var level = ctx.level().orElseThrow();
			if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(level, pos, frequency);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Component.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}

	public void handleClient(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			if (FMLEnvironment.dist == Dist.CLIENT)
				ModScreens.openBlockFrequencyScreen(frequency, pos);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Component.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}
}
