package rzk.wirelessredstone.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class WRUtils
{
	public static final int TICKS_PER_REDSTONE_TICK = 2;

	public static int clamp(int min, int max, int value)
	{
		return Math.min(Math.max(min, value), max);
	}

	public static MutableComponent positionText(BlockPos pos)
	{
		var x = Component.literal(String.valueOf(pos.getX())).withStyle(ChatFormatting.YELLOW);
		var y = Component.literal(String.valueOf(pos.getY())).withStyle(ChatFormatting.YELLOW);
		var z = Component.literal(String.valueOf(pos.getZ())).withStyle(ChatFormatting.YELLOW);
		return Component.translatable(TranslationKeys.TOOLTIP_POSITION, x, y, z).withStyle(ChatFormatting.WHITE);
	}

	public static void appendTeleportCommandIfAllowed(MutableComponent text, Player player, BlockPos pos)
	{
		if (player == null || !player.hasPermissions(2)) return;

		var command = String.format("/tp %d %d %d", pos.getX(), pos.getY() + 1, pos.getZ());
		var click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
		var hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable(TranslationKeys.MESSAGE_TELEPORT));
		text.setStyle(text.getStyle().withClickEvent(click).withHoverEvent(hover));
	}
}
