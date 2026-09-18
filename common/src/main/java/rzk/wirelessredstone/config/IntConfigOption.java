package rzk.wirelessredstone.config;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;

public class IntConfigOption implements ConfigOption {
	public final String name;
	public final int defaultValue;
	public int value;

	private final String translationKey;

	public IntConfigOption(
		String name,
		int defaultValue,
		String translationKey
	) {
		this.defaultValue = defaultValue;
		this.name = name;
		this.translationKey = translationKey;

		value = this.defaultValue;
	}

	@Override
	public void loadFromJson(JsonObject json) {
		try {
			value = json.getAsJsonPrimitive(name).getAsInt();
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
