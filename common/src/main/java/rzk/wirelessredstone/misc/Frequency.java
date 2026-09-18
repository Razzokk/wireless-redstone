package rzk.wirelessredstone.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class Frequency
{
	public static final int MIN = 0;
	public static final int MAX = 99999;
	public static final int INVALID = -1;

	public static final String NBT_KEY = "frequency";

	public static boolean isValid(int frequency) {
		return frequency >= MIN && frequency <= MAX;
	}

	public static int get(CompoundTag tag) {
		if (tag == null || !tag.contains(NBT_KEY)) return INVALID;
		return tag.getInt(NBT_KEY);
	}

	public static int get(ItemStack stack) {
		if (stack == null) return INVALID;
		return get(stack.getTag());
	}

	public static void set(CompoundTag tag, int frequency) {
		if (tag == null || !isValid(frequency)) return;
		tag.putInt(NBT_KEY, frequency);
	}

	public static void set(ItemStack stack, int frequency) {
		set(stack.getOrCreateTag(), frequency);
	}

	public static MutableComponent text(int frequency) {
		return Component.literal(String.valueOf(frequency))
			.withStyle(ChatFormatting.AQUA);
	}

	public static MutableComponent tooltip(int frequency) {
		return Component.translatable(TranslationKeys.TOOLTIP_FREQUENCY, text(frequency))
			.withStyle(ChatFormatting.GRAY);
	}
}
