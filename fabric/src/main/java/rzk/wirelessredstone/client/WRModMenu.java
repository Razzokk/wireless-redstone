package rzk.wirelessredstone.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import rzk.wirelessredstone.client.screen.ConfigScreen;

public class WRModMenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return ConfigScreen::create;
	}
}
