package rzk.wirelessredstone.client.integration;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import rzk.wirelessredstone.config.AttachmentMode;
import rzk.wirelessredstone.config.BoolConfigOption;
import rzk.wirelessredstone.config.IntConfigOption;
import rzk.wirelessredstone.config.IntRangeConfigOption;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;

import java.util.Optional;

import static rzk.wirelessredstone.misc.WRConfig.*;

public class ClothConfigScreen
{
	private static BooleanListEntry boolEntry(BoolConfigOption entry) {
		return ConfigEntryBuilder.create()
			.startBooleanToggle(entry.component(), entry.value)
			.setDefaultValue(entry.defaultValue)
			.setSaveConsumer(value -> entry.value = value)
			.build();
	}

	private static ColorEntry colorEntry(IntConfigOption entry) {
		return ConfigEntryBuilder.create()
			.startColorField(entry.component(), entry.value)
			.setDefaultValue(entry.defaultValue)
			.setSaveConsumer(color -> entry.value = color)
			.build();
	}

	private static IntegerSliderEntry sliderEntry(IntRangeConfigOption entry) {
		return ConfigEntryBuilder.create()
			.startIntSlider(entry.component(), entry.value, entry.min, entry.max)
			.setDefaultValue(entry.defaultValue)
			.setSaveConsumer(value -> entry.value = value)
			.build();
	}

	private static IntegerListEntry intEntry(IntRangeConfigOption entry) {
		return ConfigEntryBuilder.create()
			.startIntField(entry.component(), entry.value)
			.setDefaultValue(entry.defaultValue)
			.setMin(entry.min)
			.setMax(entry.max)
			.setSaveConsumer(value -> entry.value = value)
			.build();
	}

	public static Screen create(Screen parent)
	{
		ConfigBuilder builder = ConfigBuilder.create()
			.setTitle(Component.translatable(TranslationKeys.GUI_CONFIG_TITLE))
			.setParentScreen(parent)
			.setSavingRunnable(WRConfig::save);

		ConfigCategory general = builder.getOrCreateCategory(Component.translatable(TranslationKeys.GUI_CONFIG_CATEGORY_GENERAL));
		general.addEntry(sliderEntry(redstoneReceiverSignalStrength));
		general.addEntry(boolEntry(redstoneReceiverStrongPower));
		general.addEntry(
			ConfigEntryBuilder.create()
				.startEnumSelector(attachmentMode.component(), attachmentMode.clazz, attachmentMode.value)
				.setDefaultValue(attachmentMode.defaultValue)
				.setEnumNameProvider(modeRaw -> {
					if (modeRaw instanceof AttachmentMode mode) return mode.name;
					throw new RuntimeException("Unexpected enum value '" + modeRaw + "' for Attachment Mode!");
				})
				.setTooltipSupplier(mode -> Optional.of(new Component[]{mode.description}))
				.setSaveConsumer(value -> attachmentMode.value = value)
				.build());

		ConfigCategory client = builder.getOrCreateCategory(Component.translatable(TranslationKeys.GUI_CONFIG_CATEGORY_CLIENT));
		client.addEntry(colorEntry(frequencyDisplayColor));
		client.addEntry(colorEntry(linkerTargetColor));
		client.addEntry(colorEntry(highlightColor));
		client.addEntry(intEntry(highlightTimeSeconds));

		return builder.build();
	}
}
