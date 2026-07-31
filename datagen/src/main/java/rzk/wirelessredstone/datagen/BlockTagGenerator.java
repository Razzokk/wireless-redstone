package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import rzk.wirelessredstone.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider
{
	public BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture)
	{
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg)
	{
		getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(getId(ModBlocks.redstoneTransmitter))
			.add(getId(ModBlocks.redstoneReceiver))
			.add(getId(ModBlocks.p2pRedstoneTransmitter))
			.add(getId(ModBlocks.p2pRedstoneReceiver))
			.add(getId(ModBlocks.redstoneTransmitterAttachment))
			.add(getId(ModBlocks.redstoneReceiverAttachment))
			.add(getId(ModBlocks.p2pRedstoneTransmitterAttachment))
			.add(getId(ModBlocks.p2pRedstoneReceiverAttachment));
	}

	private static ResourceLocation getId(Block block)
	{
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	private static ResourceLocation getId(Item item)
	{
		return BuiltInRegistries.ITEM.getKey(item);
	}
}
