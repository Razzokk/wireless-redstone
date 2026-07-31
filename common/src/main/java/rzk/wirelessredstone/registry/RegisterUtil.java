package rzk.wirelessredstone.registry;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RegisterUtil<T> {
	void register(ResourceLocation location, T obj);
}
