package rzk.wirelessredstone.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.entity.P2pRedstoneReceiverBlockEntity;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.block.entity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;

public final class ModBlockEntities {
	public static BlockEntityType<RedstoneTransmitterBlockEntity> redstoneTransmitterBlockEntityType;
	public static BlockEntityType<RedstoneReceiverBlockEntity> redstoneReceiverBlockEntityType;
	public static BlockEntityType<P2pRedstoneTransmitterBlockEntity> p2pRedstoneTransmitterBlockEntityType;
	public static BlockEntityType<P2pRedstoneReceiverBlockEntity> p2pRedstoneReceiverBlockEntityType;

	public static void register(RegisterUtil<BlockEntityType<?>> util) {
		redstoneTransmitterBlockEntityType = registerBlockEntity(util, "redstone_transmitter_block_entity",
			BlockEntityType.Builder.of(RedstoneTransmitterBlockEntity::new, ModBlocks.redstoneTransmitter, ModBlocks.redstoneTransmitterAttachment).build(null));
		redstoneReceiverBlockEntityType = registerBlockEntity(util, "redstone_receiver_block_entity",
			BlockEntityType.Builder.of(RedstoneReceiverBlockEntity::new, ModBlocks.redstoneReceiver, ModBlocks.redstoneReceiverAttachment).build(null));
		p2pRedstoneTransmitterBlockEntityType = registerBlockEntity(util, "p2p_redstone_transmitter_block_entity",
			BlockEntityType.Builder.of(P2pRedstoneTransmitterBlockEntity::new, ModBlocks.p2pRedstoneTransmitter, ModBlocks.p2pRedstoneTransmitterAttachment).build(null));
		p2pRedstoneReceiverBlockEntityType = registerBlockEntity(util, "p2p_redstone_receiver_block_entity",
			BlockEntityType.Builder.of(P2pRedstoneReceiverBlockEntity::new, ModBlocks.p2pRedstoneReceiver, ModBlocks.p2pRedstoneReceiverAttachment).build(null));
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(RegisterUtil<BlockEntityType<?>> util, String name, BlockEntityType<T> blockEntityType) {
		var resourceLocation = new ResourceLocation(WirelessRedstone.MOD_ID, name);
		util.register(resourceLocation, blockEntityType);
		return blockEntityType;
	}
}
