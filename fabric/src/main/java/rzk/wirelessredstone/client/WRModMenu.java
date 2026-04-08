package rzk.wirelessredstone.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import rzk.wirelessredstone.client.integration.ClothConfigScreen;

public class WRModMenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return ClothConfigScreen::create;
	}
}
