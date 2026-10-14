package rzk.wirelessredstone.client.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.network.ModNetworking;

public class ClientPlatformForge implements ClientPlatform {
	@Override
	public void sendPacket(CustomPacketPayload packet) {
		ModNetworking.INSTANCE.send(packet, PacketDistributor.SERVER.noArg());
	}
}
