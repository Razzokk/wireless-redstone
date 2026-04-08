package rzk.wirelessredstone.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.integration.ClothConfigScreen;

public class WRModMenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		if (WirelessRedstone.PLATFORM.isModLoaded("cloth-config")) {
			return ClothConfigScreen::create;
		}
		return null;
	}
}
