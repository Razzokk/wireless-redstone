package rzk.wirelessredstone.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import rzk.wirelessredstone.WirelessRedstone;

import java.util.function.UnaryOperator;

public final class ModComponents {
	public static DataComponentType<Integer> frequency;
	public static DataComponentType<BlockPos> target;

	public static void register() {
		frequency = registerComponent("frequency", builder -> builder.persistent(Codec.INT));
		target = registerComponent("target", builder -> builder.persistent(BlockPos.CODEC));
	}

	private static <T> DataComponentType<T> registerComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
		var componentType = builder.apply(DataComponentType.builder()).build();
		var resourceLocation = new ResourceLocation(WirelessRedstone.MOD_ID, name);
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, resourceLocation, componentType);
		return componentType;
	}
}
