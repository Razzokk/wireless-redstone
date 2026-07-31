package rzk.wirelessredstone.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.client.render.HudRenderer;
import rzk.wirelessredstone.client.render.WorldOverlayRenderer;
import rzk.wirelessredstone.item.SnifferItem;

public class WRClientEventsForge
{
	public static void handleSnifferHighlightPacket(long timestamp, InteractionHand hand, BlockPos[] coords)
	{
		var player = Minecraft.getInstance().player;
		var stack = player.getItemInHand(hand);
		SnifferItem.setHighlightedBlocks(timestamp, stack, coords);
	}

	@SubscribeEvent
	public static void renderWorld(RenderLevelStageEvent event)
	{
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
		var level = Minecraft.getInstance().level;
		WorldOverlayRenderer.render(level, event.getCamera().getPosition(), event.getPoseStack(), event.getPartialTick());
	}

	@SubscribeEvent
	public static void renderHud(RenderGuiEvent.Post event)
	{
		HudRenderer.renderP2pTarget(event.getGuiGraphics());
	}
}
