package rzk.wirelessredstone.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class WRUtils
{
	public static final int TICKS_PER_REDSTONE_TICK = 2;

	public static final int MIN_FREQUENCY = 0;
	public static final int MAX_FREQUENCY = 99999;
	public static final int INVALID_FREQUENCY = -1;

	public static boolean isValidFrequency(int frequency)
	{
		return frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY;
	}

	public static void writeFrequency(CompoundTag tag, int frequency)
	{
		if (tag == null || !isValidFrequency(frequency)) return;
		tag.putInt(NbtKeys.FREQUENCY, frequency);
	}

	public static int readFrequency(CompoundTag tag)
	{
		if (tag == null || !tag.contains(NbtKeys.FREQUENCY)) return INVALID_FREQUENCY;
		return tag.getInt(NbtKeys.FREQUENCY);
	}

	public static void writeTarget(CompoundTag tag, BlockPos target)
	{
		if (target == null) return;
		tag.put(NbtKeys.LINKER_TARGET, NbtUtils.writeBlockPos(target));
	}

	public static BlockPos readTarget(CompoundTag tag)
	{
		if (tag == null || !tag.contains(NbtKeys.LINKER_TARGET)) return null;
		return NbtUtils.readBlockPos(tag.getCompound(NbtKeys.LINKER_TARGET));
	}

	public static int clamp(int min, int max, int value)
	{
		return Math.min(Math.max(min, value), max);
	}

	public static MutableComponent frequencyText(int frequency)
	{
		return Component.literal(String.valueOf(frequency)).withStyle(ChatFormatting.AQUA);
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
