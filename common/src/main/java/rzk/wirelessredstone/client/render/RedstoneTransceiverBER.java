package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.misc.Frequency;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.registry.ModBlocks;

public class RedstoneTransceiverBER<T extends RedstoneTransceiverBlockEntity> implements BlockEntityRenderer<T>
{
	private final Font font;
	private static final float textSurfaceOffset = 0.0078125f;

	public RedstoneTransceiverBER(BlockEntityRendererProvider.Context ctx)
	{
		font = ctx.getFont();
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay)
	{
		var frequency = entity.getFrequency();
		if (!Frequency.isValid(frequency)) return;

		var frequencyString = String.valueOf(frequency);
		var textOffset = -font.width(frequencyString) / 2.0f;

		var state = entity.getBlockState();
		poseStack.pushPose();

		if (state.is(ModBlocks.redstoneTransmitterAttachment) || state.is(ModBlocks.redstoneReceiverAttachment))
			renderAttachment(poseStack, multiBufferSource, frequencyString, textOffset, state);
		else renderBlock(poseStack, multiBufferSource, frequencyString, textOffset);

		poseStack.popPose();
	}

	private void renderBlock(PoseStack poseStack, MultiBufferSource multiBufferSource, String frequency, float textOffset)
	{
		poseStack.translate(0.5, 1, 0.5);

		for (int i = 0; i < 4; i++)
		{
			poseStack.pushPose();
			poseStack.mulPose(Axis.YP.rotation((float) (i * Math.PI / 2)));
			poseStack.translate(0, 0, 0.5 + textSurfaceOffset);
			poseStack.scale(1f / 96, -1f / 96, 1f / 96);
			font.drawInBatch(frequency, textOffset, 2.5f, WRConfig.frequencyDisplayColor.value, false, poseStack.last().pose(), multiBufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, 0xFFFFFF);
			poseStack.popPose();
		}
	}

	private void renderAttachment(PoseStack poseStack, MultiBufferSource multiBufferSource, String frequency, float textOffset, BlockState state)
	{
		var face = state.getValue(BlockStateProperties.ATTACH_FACE);
		var facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

		switch (face)
		{
			case FLOOR -> {
				poseStack.mulPose(Axis.XN.rotation((float) (Math.PI / 2)));
				var horizontal = facing.get2DDataValue() + (facing.get2DDataValue() & 1) * 2;
				poseStack.translate(0.5, -0.5, 2 / 16f + textSurfaceOffset);
				poseStack.mulPose(Axis.ZP.rotation((float) ((horizontal + 2) * Math.PI / 2)));
				poseStack.translate(0, 0.5 - 2 / 16f, 0);
			}
			case CEILING -> {
				poseStack.translate(0.5, 1 - 2 / 16f - textSurfaceOffset, 0.5);
				poseStack.mulPose(Axis.XP.rotation((float) (Math.PI / 2)));
				poseStack.mulPose(Axis.ZP.rotation((float) (facing.get2DDataValue() * Math.PI / 2)));
				poseStack.translate(0, 0.5 - 2 / 16f, 0);
			}
			case WALL -> {
				var horizontal = facing.get2DDataValue() + (facing.get2DDataValue() & 1) * 2;
				poseStack.translate(0.5, 1 - 2 / 16f, 0.5);
				poseStack.mulPose(Axis.YP.rotation((float) (horizontal * Math.PI / 2)));
				poseStack.translate(0, 0, -0.5 + 2 / 16f + textSurfaceOffset);
			}
		}

		poseStack.scale(1f / 96, -1f / 96, 1f / 96);
		font.drawInBatch(frequency, textOffset, 2.5f, WRConfig.frequencyDisplayColor.value, false, poseStack.last().pose(), multiBufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, 0xFFFFFF);

	}
}
