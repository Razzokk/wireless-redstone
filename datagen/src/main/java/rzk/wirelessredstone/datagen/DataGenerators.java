package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;

public class DataGenerators implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		generator.addProvider(ModelGenerator::new);
		generator.addProvider(BlockTagGenerator::new);
		generator.addProvider(BlockLootTableGenerator::new);
		generator.addProvider(RecipeGenerator::new);
		generator.addProvider(DefaultLanguageGenerator::new);
	}

	@Override
	public @Nullable String getEffectiveModId() {
		return WirelessRedstone.MOD_ID;
	}
}
