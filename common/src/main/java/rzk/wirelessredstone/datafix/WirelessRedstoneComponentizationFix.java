package rzk.wirelessredstone.datafix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;
import rzk.wirelessredstone.misc.Frequency;

import java.util.Set;

public class WirelessRedstoneComponentizationFix extends DataFix {
	public WirelessRedstoneComponentizationFix(Schema outputSchema) {
		super(outputSchema, false);
	}

	@Override
	protected TypeRewriteRule makeRule() {
		return writeFixAndRead("Wireless Redstone Componentization Fix",
			getInputSchema().getType(References.ITEM_STACK),
			getOutputSchema().getType(References.ITEM_STACK),
			this::fixItemStack
		);
	}

	private <T> Dynamic<T> fixItemStack(Dynamic<T> stack) {
		var id = stack.get("id").asString().result();
		if (id.isEmpty()) return stack;

		if (Set.of(
			"wirelessredstone:remote",
			"wirelessredstone:frequencySniffer",
			"wirelessredstone:frequencyTool"
		).contains(id.get())) return fixFrequency(stack);

		if ("wirelessredstone".equals(id.get()))
			return fixTarget(stack);

		return stack;
	}

	private static final String CUSTOM_DATA = "minecraft:custom_data";

	private static <T> Dynamic<T> removeCustomData(Dynamic<T> components, String dataName) {
		components = components.update(CUSTOM_DATA, data -> data.remove(dataName));
		var customData = components.get(CUSTOM_DATA).result();
		if (customData.isEmpty()) return components;

		if (components.emptyMap().equals(customData.get())) {
			components = components.remove(CUSTOM_DATA);
		}

		return components;
	}

	private <T> Dynamic<T> fixFrequency(Dynamic<T> stack) {
		var frequency = stack.get("components").get(CUSTOM_DATA).get("frequency").asInt(Frequency.INVALID);
		if (!Frequency.isValid(frequency)) return stack;

		return stack.update("components", components -> {
			components = components.set("wirelessredstone:frequency", components.createInt(frequency));
			return removeCustomData(components, "frequency");
		});
	}

	private <T> Dynamic<T> fixTarget(Dynamic<T> stack) {
		var target = stack.get("components").get(CUSTOM_DATA).get("target");
		if (target.result().isEmpty()) return stack;

		return stack.update("components", components -> {
			components = components.set("wirelessredstone:target", components.createIntList(target.asIntStream()));
			return removeCustomData(components, "target");
		});
	}
}
