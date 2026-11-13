package rzk.wirelessredstone.datafix;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;

public class ModDataFixers {
	public static void addFixers(DataFixerBuilder builder) {
		var schema1 = builder.addSchema(3818, 7, Schema::new);
		builder.addFixer(new WirelessRedstoneComponentizationFix(schema1));
	}
}
