package rzk.wirelessredstone.misc;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RegisterUtil<T>
{
	void register(ResourceLocation location, T item);
}
