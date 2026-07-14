package rzk.wirelessredstone.config;

import net.minecraft.network.chat.Component;
import rzk.wirelessredstone.misc.TranslationKeys;

public enum AttachmentMode
{
	ATTACHED(Component.translatable(TranslationKeys.GUI_CONFIG_ATTACHMENT_MODE_ATTACHED)),
	ADJACENT(Component.translatable(TranslationKeys.GUI_CONFIG_ATTACHMENT_MODE_ADJACENT));

	public final Component component;

	AttachmentMode(Component component) {
		this.component = component;
	}
}
