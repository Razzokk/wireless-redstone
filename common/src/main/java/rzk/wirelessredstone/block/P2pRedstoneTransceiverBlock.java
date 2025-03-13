package rzk.wirelessredstone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.api.RedstoneConnectable;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.item.LinkerItem;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModItems;

import static net.minecraft.state.property.Properties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public abstract class P2pRedstoneTransceiverBlock extends Block implements RedstoneConnectable, BlockEntityProvider
{
	public P2pRedstoneTransceiverBlock()
	{
		super(AbstractBlock.Settings.create()
			.mapColor(MapColor.IRON_GRAY)
			.solidBlock((state, blockGetter, pos) -> false)
			.strength(1.5F, 5.0F)
			.sounds(BlockSoundGroup.METAL));

		setDefaultState(stateManager.getDefaultState()
			.with(LINKED, false)
			.with(POWERED, false));
	}

	@Override
	public boolean hasComparatorOutput(BlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return state.get(LINKED) ? WRUtils.MAX_REDSTONE_POWER : 0;
	}

	protected abstract boolean canLink(BlockState targetState, World world, PlayerEntity player);

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (player.isSneaking()) return ActionResult.PASS;

		var stack = player.getStackInHand(hand);
		if (!stack.isOf(ModItems.linker)) return ActionResult.PASS;

		var target = LinkerItem.getTarget(stack);
		if (target == null) return ActionResult.PASS;
		if (target == pos) return ActionResult.FAIL;

		if (!world.isChunkLoaded(target))
		{
			var targetText = WRUtils.positionText(target);
			WRUtils.appendTeleportCommandIfAllowed(targetText, player, target);
			var text = Text.translatable(TranslationKeys.MESSAGE_P2P_TARGET_UNLOADED, targetText).formatted(Formatting.RED);
			player.sendMessage(text);
			return ActionResult.FAIL;
		}

		var targetState = world.getBlockState(target);
		if (!canLink(targetState, world, player)) return ActionResult.FAIL;

		var blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof P2pRedstoneTransceiverBlockEntity p2pEntity)) return ActionResult.PASS;

		var success = p2pEntity.link(target, player);
		return success ? ActionResult.SUCCESS : ActionResult.FAIL;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(LINKED, POWERED);
	}
}
