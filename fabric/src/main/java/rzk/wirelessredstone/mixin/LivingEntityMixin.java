package rzk.wirelessredstone.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rzk.wirelessredstone.api.SelectedItemListener;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> type, Level level)
	{
		super(type, level);
	}

	@Shadow
	protected ItemStack useItem;

	@Inject(method = "releaseUsingItem", at = @At("HEAD"))
	private void clearActiveItem(CallbackInfo ci)
	{
		if (useItem.getItem() instanceof SelectedItemListener listener)
			listener.onStopUsing(useItem, (LivingEntity) (Object) this, useItem.getUseDuration());
	}
}
