package rzk.wirelessredstone.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rzk.wirelessredstone.api.RedstoneConnectable;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneWireMixin
{
	@Shadow
	protected static boolean shouldConnectTo(BlockState state, Direction dir)
	{
		return false;
	}

	@Redirect(method = "getConnectingSide(" +
		"Lnet/minecraft/world/level/BlockGetter;" +
		"Lnet/minecraft/core/BlockPos;" +
		"Lnet/minecraft/core/Direction;" +
		"Z" +
		")Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;" +
			"shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"))
	private boolean onShouldConnectTo(BlockState state, Direction side, BlockGetter level, BlockPos pos, Direction direction)
	{
		if (!(state.getBlock() instanceof RedstoneConnectable connectable))
			return shouldConnectTo(state, direction);

		return connectable.canConnectRedstone(state, level, pos.relative(direction), direction);
	}
}
