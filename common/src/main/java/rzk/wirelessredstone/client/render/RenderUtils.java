package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RenderUtils
{
	public static void drawLine(BufferBuilder builder, PoseStack.Pose pose, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float red, float green, float blue, float alpha)
	{
		var lenX = maxX - minX;
		var lenY = maxY - minY;
		var lenZ = maxZ - minZ;
		var len = Mth.sqrt(lenX * lenX + lenY * lenY + lenZ * lenZ);

		lenX /= len;
		lenY /= len;
		lenZ /= len;

		builder.vertex(pose.pose(), minX, minY, minZ)
			.color(red, green, blue, alpha)
			.normal(pose.normal(), lenX, lenY, lenZ)
			.endVertex();

		builder.vertex(pose.pose(), maxX, maxY, maxZ)
			.color(red, green, blue, alpha)
			.normal(pose.normal(), lenX, lenY, lenZ)
			.endVertex();
	}

	public static void drawOutlineShape(BufferBuilder builder, PoseStack.Pose pose, VoxelShape shape, BlockPos pos, float red, float green, float blue, float alpha)
	{
		shape.forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) ->
			drawLine(builder, pose,
				(float) (pos.getX() + minX),
				(float) (pos.getY() + minY),
				(float) (pos.getZ() + minZ),
				(float) (pos.getX() + maxX),
				(float) (pos.getY() + maxY),
				(float) (pos.getZ() + maxZ),
				red, green, blue, alpha)
		);
	}
}
