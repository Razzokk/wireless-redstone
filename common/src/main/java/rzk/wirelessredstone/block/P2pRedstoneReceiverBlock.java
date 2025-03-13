package rzk.wirelessredstone.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.P2pRedstoneReceiverBlockEntity;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;

import static net.minecraft.state.property.Properties.POWERED;

public class P2pRedstoneReceiverBlock extends P2pRedstoneTransceiverBlock
{
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if (state.getBlock() != newState.getBlock())
		{
			world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneReceiverBlockEntityType)
				.ifPresent(P2pRedstoneTransceiverBlockEntity::unlinkOther);
		}

		if (!world.isClient && WRConfig.redstoneReceiverStrongPower)
			for (Direction direction : DIRECTIONS)
				world.updateNeighborsExcept(pos.offset(direction), this, direction.getOpposite());

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return state.get(POWERED) && connectsToRedstone(state, world, pos, direction) ?
			WRConfig.redstoneReceiverSignalStrength : 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return WRConfig.redstoneReceiverStrongPower ? getWeakRedstonePower(state, world, pos, direction) : 0;
	}

	@Override
	protected boolean canLink(BlockState targetState, World world, PlayerEntity player)
	{
		if (targetState.isOf(ModBlocks.p2pRedstoneTransmitter)) return true;

		if (!world.isClient)
		{
			var receiverTranslated = Text
				.translatable(ModBlocks.p2pRedstoneTransmitter.getTranslationKey())
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
		return new P2pRedstoneReceiverBlockEntity(pos, state);
	}
}
