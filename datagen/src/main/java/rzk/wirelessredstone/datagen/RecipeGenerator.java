package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

import java.util.function.Consumer;

public class RecipeGenerator extends FabricRecipeProvider {
	public RecipeGenerator(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateRecipes(Consumer<FinishedRecipe> exporter) {
		{
			ShapedRecipeBuilder.shaped(ModItems.circuit, 2)
				.pattern("RGR")
				.pattern("IEI")
				.pattern("QGQ")
				.define('R', Items.REDSTONE)
				.define('G', Items.GLOWSTONE_DUST)
				.define('I', Items.GOLD_INGOT)
				.define('E', Items.ENDER_PEARL)
				.define('Q', Items.QUARTZ)
				.unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
				.unlockedBy(getHasName(Items.GLOWSTONE_DUST), has(Items.GLOWSTONE_DUST))
				.unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
				.unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
				.unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.redstoneTransmitter)
				.pattern("IRI")
				.pattern("RCR")
				.pattern("IRI")
				.define('I', Items.IRON_INGOT)
				.define('R', Items.REDSTONE_TORCH)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.redstoneReceiver)
				.pattern("IRI")
				.pattern("RCR")
				.pattern("IRI")
				.define('I', Items.IRON_INGOT)
				.define('R', Items.REDSTONE)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.p2pRedstoneTransmitter)
				.pattern("IRI")
				.pattern("RCR")
				.pattern("IRI")
				.define('I', Items.COPPER_INGOT)
				.define('R', Items.REDSTONE_TORCH)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.p2pRedstoneReceiver)
				.pattern("IRI")
				.pattern("RCR")
				.pattern("IRI")
				.define('I', Items.COPPER_INGOT)
				.define('R', Items.REDSTONE)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.redstoneTransmitterAttachment)
				.pattern(" I ")
				.pattern("RCR")
				.define('I', Items.IRON_INGOT)
				.define('R', Items.REDSTONE_TORCH)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.redstoneReceiverAttachment)
				.pattern(" I ")
				.pattern("RCR")
				.define('I', Items.IRON_INGOT)
				.define('R', Items.REDSTONE)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.p2pRedstoneTransmitterAttachment)
				.pattern(" I ")
				.pattern("RCR")
				.define('I', Items.COPPER_INGOT)
				.define('R', Items.REDSTONE_TORCH)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModBlocks.p2pRedstoneReceiverAttachment)
				.pattern(" I ")
				.pattern("RCR")
				.define('I', Items.COPPER_INGOT)
				.define('R', Items.REDSTONE)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModItems.frequencyTool)
				.pattern("RTR")
				.pattern("ICI")
				.pattern(" I ")
				.define('R', Items.REDSTONE)
				.define('T', Items.COMPARATOR)
				.define('I', Items.IRON_INGOT)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModItems.frequencySniffer)
				.pattern("ITI")
				.pattern("ICI")
				.pattern("ITI")
				.define('T', Items.COMPARATOR)
				.define('I', Items.IRON_INGOT)
				.define('C', ModItems.circuit)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModItems.remote)
				.pattern("TPT")
				.pattern("ICI")
				.pattern("IRI")
				.define('T', Items.REDSTONE_TORCH)
				.define('P', Items.ENDER_PEARL)
				.define('I', Items.IRON_INGOT)
				.define('C', ModItems.circuit)
				.define('R', Items.REDSTONE)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);

			ShapedRecipeBuilder.shaped(ModItems.linker)
				.pattern("TAT")
				.pattern("ICI")
				.pattern("RAR")
				.define('T', Items.REDSTONE_TORCH)
				.define('A', Items.AMETHYST_SHARD)
				.define('I', Items.IRON_INGOT)
				.define('C', ModItems.circuit)
				.define('R', Items.REDSTONE)
				.unlockedBy(getHasName(ModItems.circuit), has(ModItems.circuit))
				.save(exporter);
		}
	}
}
