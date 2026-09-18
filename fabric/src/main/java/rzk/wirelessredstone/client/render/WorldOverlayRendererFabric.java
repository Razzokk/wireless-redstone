package rzk.wirelessredstone.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

public class WorldOverlayRendererFabric
{
	public static void render(WorldRenderContext worldRenderContext)
	{
		var world = worldRenderContext.world();
		var cameraPosition = worldRenderContext.camera().getPosition();
		var matrixStack = worldRenderContext.matrixStack();
		var tickDelta = worldRenderContext.tickDelta();
		WorldOverlayRenderer.render(world, cameraPosition, matrixStack, tickDelta);
	}
}
