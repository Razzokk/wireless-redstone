package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import rzk.wirelessredstone.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider
{
	protected BlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
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
