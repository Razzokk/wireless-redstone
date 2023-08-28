package rzk.wirelessredstone.client.screen;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;

public class ConfigScreen
{
	public static Screen get(Screen parent)
	{
		ConfigBuilder builder = ConfigBuilder.create()
			.setTitle(new TranslatableComponent(TranslationKeys.GUI_CONFIG_TITLE))
			.setParentScreen(parent)
			.setSavingRunnable(WRConfig::save);

		ConfigCategory general = builder.getOrCreateCategory(new TranslatableComponent(TranslationKeys.GUI_CONFIG_CATEGORY_GENERAL));

		general.addEntry(ConfigEntryBuilder.create()
			.startIntSlider(new TranslatableComponent(TranslationKeys.GUI_CONFIG_SIGNAL_STRENGTH), WRConfig.redstoneReceiverSignalStrength, 1, 15)
			.setDefaultValue(15)
			.setSaveConsumer(strength -> WRConfig.redstoneReceiverSignalStrength = strength)
			.build());

		general.addEntry(ConfigEntryBuilder.create()
			.startBooleanToggle(new TranslatableComponent(TranslationKeys.GUI_CONFIG_STRONG_POWER), WRConfig.redstoneReceiverStrongPower)
			.setDefaultValue(true)
			.setSaveConsumer(strongPower -> WRConfig.redstoneReceiverStrongPower = strongPower)
			.build());

		ConfigCategory client = builder.getOrCreateCategory(new TranslatableComponent(TranslationKeys.GUI_CONFIG_CATEGORY_CLIENT));

		client.addEntry(ConfigEntryBuilder.create()
			.startColorField(new TranslatableComponent(TranslationKeys.GUI_CONFIG_DISPLAY_COLOR), WRConfig.frequencyDisplayColor)
			.setDefaultValue(0)
			.setSaveConsumer(color -> WRConfig.frequencyDisplayColor = color)
			.build());

		client.addEntry(ConfigEntryBuilder.create()
			.startColorField(new TranslatableComponent(TranslationKeys.GUI_CONFIG_HIGHLIGHT_COLOR), WRConfig.highlightColor)
			.setDefaultValue(0xFF3F3F)
			.setSaveConsumer(color -> WRConfig.highlightColor = color)
			.build());

		client.addEntry(ConfigEntryBuilder.create()
			.startIntField(new TranslatableComponent(TranslationKeys.GUI_CONFIG_HIGHLIGHT_TIME), WRConfig.highlightTimeSeconds)
			.setDefaultValue(10)
			.setMin(1)
			.setSaveConsumer(seconds -> WRConfig.highlightTimeSeconds = seconds)
			.build());

		return builder.build();
	}
}
