package rzk.wirelessredstone.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import net.minecraft.util.datafix.DataFixers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rzk.wirelessredstone.datafix.ModDataFixers;

@Mixin(DataFixers.class)
public class DataFixersMixin {
	@Inject(method = "addFixers", at = @At(value = "TAIL"), remap = false)
	private static void onAddFixers(DataFixerBuilder builder, CallbackInfo ci) {
		ModDataFixers.addFixers(builder);
	}
}
