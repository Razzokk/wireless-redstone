package rzk.wirelessredstone.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import rzk.wirelessredstone.client.render.WorldOverlayRenderer;
import rzk.wirelessredstone.item.SnifferItem;

public class WRClientEventsNeo
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
}
