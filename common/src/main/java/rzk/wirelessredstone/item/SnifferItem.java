package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.NbtKeys;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.config.WRConfig;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.network.SnifferHighlightPacket;
import rzk.wirelessredstone.registry.ModItems;

import java.util.ArrayList;
import java.util.Set;

public class SnifferItem extends FrequencyItem
{
	public SnifferItem(Properties properties)
	{
		super(properties);
	}

	public static BlockPos[] getHighlightedBlocks(ItemStack stack)
	{
		if (!stack.is(ModItems.frequencySniffer)) return null;

		var tag = stack.getTag();
		if (tag == null) return null;

		var list = tag.getList(NbtKeys.HIGHLIGHTS, Tag.TAG_INT_ARRAY);
		if (list.isEmpty()) return null;

		var coords = new BlockPos[list.size()];
		for (int i = 0; i < list.size(); ++i)
			coords[i] = WRUtils.readBlockPos(list.get(i));

		return coords;
	}

	public static void setHighlightedBlocks(long timestamp, ItemStack stack, BlockPos[] coords)
	{
		if (!stack.is(ModItems.frequencySniffer)) return;

		var tag = stack.getOrCreateTag();
		tag.putLong(NbtKeys.TIMESTAMP, timestamp);

		var list = new ListTag();
		for (var pos : coords)
			list.add(WRUtils.writeBlockPos(pos));
		tag.put(NbtKeys.HIGHLIGHTS, list);
	}

	private static void removeHighlightBlocks(ItemStack stack)
	{
		var tag = stack.getTag();
		if (tag == null) return;

		tag.remove("timestamp");
		tag.remove("highlights");
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if (!context.getPlayer().isShiftKeyDown())
			return InteractionResult.PASS;
		return super.useOn(context);
	}

	private Component buildActiveTransmittersMessage(Player player, Set<BlockPos> transmitters, Component frequencyText)
	{
		var texts = new ArrayList<Component>();
		texts.add(Component.translatable(TranslationKeys.MESSAGE_TRANSMITTERS_ACTIVE, frequencyText, transmitters.size()));

		for (var pos : transmitters)
		{
			if (texts.size() > 20)
			{
				texts.add(Component.literal("..."));
				break;
			}

			var text = WRUtils.positionText(pos);
			WRUtils.appendTeleportCommandIfAllowed(text, player, pos);
			texts.add(text);
		}

		return ComponentUtils.formatList(texts, Component.literal("\n"));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		if (player.isShiftKeyDown())  return super.use(level, player, usedHand);

		ItemStack stack = player.getItemInHand(usedHand);
		int frequency = Frequency.get(stack);

		if (!Frequency.isValid(frequency))
		{
			if (level.isClientSide)
				player.displayClientMessage(Component.translatable(TranslationKeys.MESSAGE_NO_FREQUENCY).withStyle(ChatFormatting.RED), true);
			return InteractionResultHolder.fail(stack);
		}

		player.getCooldowns().addCooldown(this, SharedConstants.TICKS_PER_SECOND);
		var result = InteractionResultHolder.success(stack);
		if (level.isClientSide) return result;

		var frequencyText = Frequency.text(frequency);
		var ether = RedstoneEther.get((ServerLevel) level);

		if (ether == null)
		{
			player.sendSystemMessage(Component.translatable(TranslationKeys.MESSAGE_TRANSMITTERS_EMPTY, frequencyText));
			return result;
		}

		var transmitters = ether.getTransmitters(frequency);

		if (transmitters.isEmpty())
		{
			player.sendSystemMessage(Component.translatable(TranslationKeys.MESSAGE_TRANSMITTERS_EMPTY, frequencyText));
			removeHighlightBlocks(stack);
		}
		else
		{
			var message = buildActiveTransmittersMessage(player, transmitters, frequencyText);
			player.sendSystemMessage(message);
			var coords = transmitters.stream().filter(pos -> player.shouldRender(pos.getX(), pos.getY(), pos.getZ())).toArray(BlockPos[]::new);
			var packet = new SnifferHighlightPacket(level.getGameTime(), usedHand, coords);
			WirelessRedstone.PLATFORM.sendPacket((ServerPlayer) player, packet);
		}

		return result;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected)
	{
		var tag = stack.getTag();
		if (!isSelected || !level.isClientSide || tag == null) return;

		var timeOffset = (long) WRConfig.highlightTimeSeconds.value * SharedConstants.TICKS_PER_SECOND;
		if (level.getGameTime() >= tag.getLong("timestamp") + timeOffset)
			removeHighlightBlocks(stack);
	}
}
