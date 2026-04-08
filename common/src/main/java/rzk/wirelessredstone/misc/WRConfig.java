package rzk.wirelessredstone.misc;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.level.redstone.Redstone;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.config.BoolConfigOption;
import rzk.wirelessredstone.config.IntConfigOption;
import rzk.wirelessredstone.config.IntRangeConfigOption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WRConfig
{
	private static final Gson GSON = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		.setPrettyPrinting()
		.create();

	private static final String FILE_NAME = WirelessRedstone.MOD_ID + ".json";

	// General
	public static IntRangeConfigOption redstoneReceiverSignalStrength = new IntRangeConfigOption("signal_strength", Redstone.SIGNAL_MAX, 1, Redstone.SIGNAL_MAX, TranslationKeys.GUI_CONFIG_SIGNAL_STRENGTH);
	public static BoolConfigOption redstoneReceiverStrongPower = new BoolConfigOption("provide_strong_power", true, TranslationKeys.GUI_CONFIG_STRONG_POWER);

	// Client
	public static IntConfigOption frequencyDisplayColor = new IntConfigOption("display_color", 0, TranslationKeys.GUI_CONFIG_DISPLAY_COLOR);
	public static IntConfigOption linkerTargetColor = new IntConfigOption("linker_target_color", 0x32C8FF, TranslationKeys.GUI_CONFIG_TARGET_COLOR);
	public static IntConfigOption highlightColor = new IntConfigOption("highlight_color", 0xFF3F3F, TranslationKeys.GUI_CONFIG_HIGHLIGHT_COLOR);
	public static IntRangeConfigOption highlightTimeSeconds = new IntRangeConfigOption("highlight_time", 10, 1, Integer.MAX_VALUE, TranslationKeys.GUI_CONFIG_HIGHLIGHT_TIME);

	public static void load()
	{
		File file = new File(WirelessRedstone.PLATFORM.getConfigDir(), FILE_NAME);

		if (!file.exists()) save();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
		{
			JsonObject config = JsonParser.parseReader(bufferedReader).getAsJsonObject();

			// General
			redstoneReceiverSignalStrength.loadFromJson(config);
			redstoneReceiverStrongPower.loadFromJson(config);

			// Client
			frequencyDisplayColor.loadFromJson(config);
			highlightColor.loadFromJson(config);
			highlightTimeSeconds.loadFromJson(config);
			linkerTargetColor.loadFromJson(config);
		}
		catch (IOException | NullPointerException e)
		{
			WirelessRedstone.LOGGER.error("Couldn't load Wireless Redstone configs from file");
		}
	}

	public static void save()
	{
		File file = new File(WirelessRedstone.PLATFORM.getConfigDir(), FILE_NAME);
		JsonObject config = new JsonObject();

		// General
		redstoneReceiverSignalStrength.writeToJson(config);
		redstoneReceiverStrongPower.writeToJson(config);

		// Client
		frequencyDisplayColor.writeToJson(config);
		highlightColor.writeToJson(config);
		highlightTimeSeconds.writeToJson(config);
		linkerTargetColor.writeToJson(config);

		try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file)))
		{
			fileWriter.write(GSON.toJson(config));
		}
		catch (IOException e)
		{
			WirelessRedstone.LOGGER.error("Couldn't save Wireless Redstone configs to file", e);
		}
	}
}
