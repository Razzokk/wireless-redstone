package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rzk.wirelessredstone.item.LinkerItem;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.misc.WRConfig;

public class WorldOverlayRenderer
{
	public static void render(Level level, Vec3 cameraPosition, PoseStack poseStack, float tickDelta)
	{
		var player = Minecraft.getInstance().player;
		var stack = player.getMainHandItem();
		renderSnifferHighlights(level, player, stack, cameraPosition, poseStack);
		renderLinkerTarget(level, player, stack, cameraPosition, poseStack, tickDelta);
	}

	private static Tesselator renderLinesPre()
	{
		RenderSystem.assertOnRenderThread();
		GlStateManager._depthMask(false);
		GlStateManager._disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.lineWidth(3f);
		RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
		return RenderSystem.renderThreadTesselator();
	}

	private static void renderLinesPost(Tesselator tesselator)
	{
		tesselator.end();
		RenderSystem.enableDepthTest();
		RenderSystem.lineWidth(1f);
		GlStateManager._enableCull();
		GlStateManager._depthMask(true);
	}

	private static void renderSnifferHighlights(Level level, Player player, ItemStack stack, Vec3 cameraPosition, PoseStack poseStack)
	{
		var coords = SnifferItem.getHighlightedBlocks(stack);
		if (coords == null) coords = SnifferItem.getHighlightedBlocks(player.getOffhandItem());
		if (coords == null) return;

		var red = ((WRConfig.highlightColor.value >> 16) & 0xFF) / 256.0f;
		var green = ((WRConfig.highlightColor.value >> 8) & 0xFF) / 256.0f;
		var blue = (WRConfig.highlightColor.value & 0xFF) / 256.0f;

		var tesselator = renderLinesPre();
		var builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

		var pose = poseStack.last();

		for (var pos : coords)
		{
			if (!player.shouldRender(pos.getX(), pos.getY(), pos.getZ())) continue;

			var state = level.getBlockState(pos);
			var shape = state.getShape(level, pos);
			RenderUtils.drawOutlineShape(builder, pose, cameraPosition, shape, pos, red, green, blue, 1);
		}

		renderLinesPost(tesselator);
	}

	private static void renderLinkerTarget(Level level, Player player, ItemStack stack, Vec3 cameraPosition, PoseStack poseStack, float tickDelta)
	{
		BlockPos target = LinkerItem.getTarget(stack);
		if (target == null) target = LinkerItem.getTarget(player.getOffhandItem());
		if (target == null || !player.shouldRender(target.getX(), target.getY(), target.getZ())) return;

		var color = WRConfig.linkerTargetColor.value;
		var red = ((color >> 16) & 0xFF) / 256.0f;
		var green = ((color >> 8) & 0xFF) / 256.0f;
		var blue = (color & 0xFF) / 256.0f;

		var tesselator = renderLinesPre();
		var builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

		var time = ((level.getGameTime() + tickDelta) % 40) / 20;
		float alpha;

		if (time <= 1)
		{
			alpha = (3 - 2 * time) * time * time;
		}
		else
		{
			time -= 1;
			alpha = 1 - (3 - 2 * time) * time * time;
		}

		var state = level.getBlockState(target);
		var shape = state.getShape(level, target);
		var pose = poseStack.last();
		RenderUtils.drawOutlineShape(builder, pose, cameraPosition, shape, target, red, green, blue, alpha);

		renderLinesPost(tesselator);
	}
}
