package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModItems;

public class HudRenderer
{
	public static void renderP2pTarget(PoseStack poseStack) {
		var mc = Minecraft.getInstance();
		var player = mc.player;

		if (!player.getMainHandItem().is(ModItems.linker) && !player.getOffhandItem().is(ModItems.linker)) return;

		var hitResult = mc.hitResult;
		if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) return;

		var pos = ((BlockHitResult) hitResult).getBlockPos();

		if (mc.level.getBlockEntity(pos) instanceof P2pRedstoneTransceiverBlockEntity be && be.getLink() != null)
		{
			var targetText = WRUtils.targetText(be.getLink());
			var height = mc.getWindow().getGuiScaledHeight();
			var width = mc.getWindow().getGuiScaledWidth();
			GuiComponent.drawCenteredString(poseStack, mc.font, targetText, width / 2, height / 2 + 12, 0xFFFFFF);
		}
	}
}
