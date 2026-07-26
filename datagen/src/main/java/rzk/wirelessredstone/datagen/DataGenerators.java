package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;

public class DataGenerators implements DataGeneratorEntrypoint
{
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(BlockTagGenerator::new);
		pack.addProvider(BlockLootTableGenerator::new);
		pack.addProvider(RecipeGenerator::new);
		pack.addProvider(DefaultLanguageGenerator::new);
	}

	@Override
	public @Nullable String getEffectiveModId() {
		return WirelessRedstone.MOD_ID;
	}
}
