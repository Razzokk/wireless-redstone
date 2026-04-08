package rzk.wirelessredstone.config;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;

public class IntRangeConfigOption implements ConfigOption {
	public final String name;
	public final int defaultValue;
	public final int min;
	public final int max;
	public int value;

	private final String translationKey;

	public IntRangeConfigOption(
		String name,
		int defaultValue,
		int min,
		int max,
		String translationKey
	) {
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
		this.name = name;
		this.translationKey = translationKey;
	}

	@Override
	public void loadFromJson(JsonObject json) {
		try {
			var val = json.getAsJsonPrimitive(name).getAsInt();
			if (val < min) val = min;
			else if (val > max) val = max;
			value = val;
		}
		catch (Exception ignored) {
			value = defaultValue;
		}
	}

	@Override
	public void writeToJson(JsonObject json) {
		json.addProperty(name, value);
	}

	@Override
	public Component component()
	{
		return Component.translatable(translationKey);
	}
}
