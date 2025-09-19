package rzk.wirelessredstone.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rzk.wirelessredstone.api.SelectedItemListener;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player
{
	public ServerPlayerMixin(Level level, BlockPos pos, float yaw, GameProfile gameProfile)
	{
		super(level, pos, yaw, gameProfile);
	}

	@Inject(method = "drop(Z)Z", at = @At("HEAD"))
	public void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir)
	{
		var stack = getMainHandItem();
		if (stack.isEmpty()) return;

		if (stack.getItem() instanceof SelectedItemListener listener)
			listener.onDroppedByPlayer(entireStack ? stack : stack.copyWithCount(1), (Player) (Object) this);
	}
}
