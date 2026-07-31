package rzk.wirelessredstone.config;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;

public class EnumConfigOption<T extends Enum<T>> implements ConfigOption {
	public final String name;
	public final Class<T> clazz;
	public final T defaultValue;
	public T value;

	private final String translationKey;

	public EnumConfigOption(
		String name,
		Class<T> clazz,
		T defaultValue,
		String translationKey
	) {
		this.defaultValue = defaultValue;
		this.name = name;
		this.clazz = clazz;
		this.translationKey = translationKey;

		value = this.defaultValue;
	}

	@Override
	public void loadFromJson(JsonObject json) {
		try {
			var jsonString = json.getAsJsonPrimitive(name).getAsString();
			value = Enum.valueOf(clazz, jsonString);
		}
		catch (Exception ignored) {
			value = defaultValue;
		}
	}

	@Override
	public void writeToJson(JsonObject json) {
		json.addProperty(name, value.toString());
	}

	@Override
	public Component component() {
		return Component.translatable(translationKey);
	}
}
