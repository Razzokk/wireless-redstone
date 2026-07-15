package rzk.wirelessredstone.config;

import net.minecraft.network.chat.Component;
import rzk.wirelessredstone.misc.TranslationKeys;

public enum AttachmentMode
{
	ATTACHED(
		Component.translatable(TranslationKeys.GUI_CONFIG_ATTACHMENT_MODE_ATTACHED),
		Component.translatable(TranslationKeys.GUI_CONFIG_ATTACHMENT_MODE_ATTACHED_DESCRIPTION)
	),
	ADJACENT(
		Component.translatable(TranslationKeys.GUI_CONFIG_ATTACHMENT_MODE_ADJACENT),
		Component.translatable(TranslationKeys.GUI_CONFIG_ATTACHMENT_MODE_ADJACENT_DESCRIPTION)
	);

	public final Component name;
	public final Component description;

	AttachmentMode(Component name, Component description) {
		this.name = name;
		this.description = description;
	}
}
