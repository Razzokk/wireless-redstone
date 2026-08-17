package rzk.wirelessredstone.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.level.redstone.Redstone;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.misc.TranslationKeys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WRConfig {
	private static final Gson GSON = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		.setPrettyPrinting()
		.create();

	private static final String FILE_NAME = WirelessRedstone.MOD_ID + ".json";
	private static final List<ConfigOption> CONFIGS = new ArrayList<>();

	private static <T extends ConfigOption> T addConfig(T configOption) {
		CONFIGS.add(configOption);
		return configOption;
	}

	// General
	public static IntRangeConfigOption redstoneReceiverSignalStrength = addConfig(new IntRangeConfigOption("signal_strength", Redstone.SIGNAL_MAX, 1, Redstone.SIGNAL_MAX, TranslationKeys.GUI_CONFIG_SIGNAL_STRENGTH));
	public static BoolConfigOption redstoneReceiverStrongPower = addConfig(new BoolConfigOption("provide_strong_power", true, TranslationKeys.GUI_CONFIG_STRONG_POWER));

	// Client
	public static IntConfigOption frequencyDisplayColor = addConfig(new IntConfigOption("display_color", 0, TranslationKeys.GUI_CONFIG_DISPLAY_COLOR));
	public static IntConfigOption linkerTargetColor = addConfig(new IntConfigOption("linker_target_color", 0x32C8FF, TranslationKeys.GUI_CONFIG_TARGET_COLOR));
	public static IntConfigOption highlightColor = addConfig(new IntConfigOption("highlight_color", 0xFF3F3F, TranslationKeys.GUI_CONFIG_HIGHLIGHT_COLOR));
	public static IntRangeConfigOption highlightTimeSeconds = addConfig(new IntRangeConfigOption("highlight_time", 10, 1, Integer.MAX_VALUE, TranslationKeys.GUI_CONFIG_HIGHLIGHT_TIME));

	public static void load() {
		File file = new File(WirelessRedstone.PLATFORM.getConfigDir(), FILE_NAME);

		if (!file.exists()) {
			save();
			return;
		}

		try (var reader = new BufferedReader(new FileReader(file))) {
			var config = JsonParser.parseReader(reader).getAsJsonObject();

			for (var configOption : CONFIGS)
				configOption.loadFromJson(config);
		}
		catch (IOException | NullPointerException e) {
			WirelessRedstone.LOGGER.error("Couldn't load Wireless Redstone configs from file");
		}
	}

	public static void save() {
		var file = new File(WirelessRedstone.PLATFORM.getConfigDir(), FILE_NAME);
		var config = new JsonObject();

		for (var configOption : CONFIGS)
			configOption.writeToJson(config);

		try (var writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(GSON.toJson(config));
		}
		catch (IOException e) {
			WirelessRedstone.LOGGER.error("Couldn't save Wireless Redstone configs to file", e);
		}
	}
}
