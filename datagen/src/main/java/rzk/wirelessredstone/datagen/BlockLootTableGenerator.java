package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import rzk.wirelessredstone.registry.ModBlocks;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider
{
	protected BlockLootTableGenerator(FabricDataOutput dataOutput)
	{
		super(dataOutput);
	}

	@Override
	public void generate()
	{
		dropSelf(ModBlocks.redstoneTransmitter);
		dropSelf(ModBlocks.redstoneReceiver);
		dropSelf(ModBlocks.p2pRedstoneTransmitter);
		dropSelf(ModBlocks.p2pRedstoneReceiver);

		dropSelf(ModBlocks.redstoneTransmitterAttachment);
		dropSelf(ModBlocks.redstoneReceiverAttachment);
		dropSelf(ModBlocks.p2pRedstoneTransmitterAttachment);
		dropSelf(ModBlocks.p2pRedstoneReceiverAttachment);
	}
}
