package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;

import static net.minecraft.state.property.Properties.POWERED;

public class P2pRedstoneTransmitterBlock extends P2pRedstoneTransceiverBlock implements BlockEntityProvider
{
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		var world = ctx.getWorld();
		var pos = ctx.getBlockPos();
		var state = getDefaultState();

		return state.with(POWERED, isReceivingRedstonePower(state, world, pos));
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify)
	{
		if (!state.get(POWERED)) return;

		world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
			.ifPresent(P2pRedstoneTransmitterBlockEntity::scheduleReceiverUpdate);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if (state.getBlock() != newState.getBlock())
		{
			world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
				.ifPresent(P2pRedstoneTransceiverBlockEntity::unlinkOther);
		}
		else if (state.get(POWERED))
		{
			world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
				.ifPresent(P2pRedstoneTransmitterBlockEntity::scheduleReceiverUpdate);
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
			.ifPresent(P2pRedstoneTransmitterBlockEntity::updateReceiver);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify)
	{
		if (world.isClient) return;
		var powered = isReceivingRedstonePower(state, world, pos);

		if (state.get(POWERED) == powered) return;
		world.setBlockState(pos, state.with(POWERED, powered), NOTIFY_LISTENERS);
	}

	@Override
	protected boolean canLink(BlockState targetState, World world, PlayerEntity player)
	{
		if (targetState.isOf(ModBlocks.p2pRedstoneReceiver)) return true;

		if (!world.isClient)
		{
			var receiverTranslated = Text
				.translatable(ModBlocks.p2pRedstoneReceiver.getTranslationKey())
				.formatted(Formatting.AQUA);
			var text = Text.translatable(TranslationKeys.MESSAGE_P2P_WRONG_TARGET, receiverTranslated);
			player.sendMessage(text);
		}

		return false;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new P2pRedstoneTransmitterBlockEntity(pos, state);
	}
}
