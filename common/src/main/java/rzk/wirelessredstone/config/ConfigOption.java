package rzk.wirelessredstone.config;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;

public interface ConfigOption
{
	void loadFromJson(JsonObject json);

	void writeToJson(JsonObject json);

	Component component();
}
