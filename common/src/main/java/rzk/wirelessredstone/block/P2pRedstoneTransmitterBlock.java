package rzk.wirelessredstone.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class P2pRedstoneTransmitterBlock extends P2pRedstoneTransceiverBlock
{
	protected boolean hasSignal(BlockState state, Level level, BlockPos pos)
	{
		return level.hasNeighborSignal(pos);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
	{
		var world = context.getLevel();
		var pos = context.getClickedPos();
		var state = defaultBlockState();
		return state.setValue(POWERED, hasSignal(state, world, pos));
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		if (!state.getValue(POWERED)) return;

		level.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
			.ifPresent(P2pRedstoneTransmitterBlockEntity::scheduleReceiverUpdate);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (state.getBlock() != newState.getBlock())
		{
			level.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
				.ifPresent(P2pRedstoneTransceiverBlockEntity::unlinkOther);
		}
		else if (state.getValue(POWERED))
		{
			level.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
				.ifPresent(P2pRedstoneTransmitterBlockEntity::scheduleReceiverUpdate);
		}

		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{

		level.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
			.ifPresent(P2pRedstoneTransmitterBlockEntity::updateReceiver);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		if (level.isClientSide) return;
		var powered = hasSignal(state, level, pos);

		if (state.getValue(POWERED) == powered) return;
		level.setBlock(pos, state.setValue(POWERED, powered), UPDATE_CLIENTS);
	}

	@Override
	protected boolean canLink(BlockState targetState, Level level, Player player)
	{
		if (targetState.is(ModBlocks.p2pRedstoneReceiver) || targetState.is(ModBlocks.p2pRedstoneReceiverAttachment)) return true;

		if (!level.isClientSide)
		{
			var receiverTranslated = ModBlocks.p2pRedstoneReceiver.getName().withStyle(ChatFormatting.AQUA);
			var text = Component.translatable(TranslationKeys.MESSAGE_P2P_WRONG_TARGET, receiverTranslated);
			player.sendSystemMessage(text);
		}

		return false;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new P2pRedstoneTransmitterBlockEntity(pos, state);
	}
}
