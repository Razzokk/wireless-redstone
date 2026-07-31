package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
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

	public static void drawOutlineShape(BufferBuilder builder, PoseStack.Pose pose, Vec3 cameraPos, VoxelShape shape, BlockPos pos, float red, float green, float blue, float alpha)
	{
		shape.forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) ->
			drawLine(builder, pose,
				(float) (pos.getX() + minX - cameraPos.x),
				(float) (pos.getY() + minY - cameraPos.y),
				(float) (pos.getZ() + minZ - cameraPos.z),
				(float) (pos.getX() + maxX - cameraPos.x),
				(float) (pos.getY() + maxY - cameraPos.y),
				(float) (pos.getZ() + maxZ - cameraPos.z),
				red, green, blue, alpha)
		);
	}
}
