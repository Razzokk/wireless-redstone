package rzk.wirelessredstone.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.AttachFace;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public class ModelGenerator extends FabricModelProvider
{
	public ModelGenerator(FabricDataOutput output)
	{
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generators)
	{
		transceiverBlock(generators, ModBlocks.redstoneTransmitter);
		transceiverBlock(generators, ModBlocks.redstoneReceiver);
		transceiverAttachmentBlock(generators, ModBlocks.redstoneTransmitterAttachment);
		transceiverAttachmentBlock(generators, ModBlocks.redstoneReceiverAttachment);
		p2pTransceiverBlock(generators, ModBlocks.p2pRedstoneTransmitter);
		p2pTransceiverBlock(generators, ModBlocks.p2pRedstoneReceiver);
	}

	@Override
	public void generateItemModels(ItemModelGenerators generators)
	{
		generators.generateFlatItem(ModItems.circuit, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(ModItems.frequencyTool, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(ModItems.frequencySniffer, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(ModItems.linker, ModelTemplates.FLAT_ITEM);

		registerOverrides(generators, ModItems.remote, TextureMapping.layer0(TextureMapping.getItemTexture(ModItems.remote, "_off")),
			new ItemOverride("state", 1, (key, state) ->
				new Tuple<>(ModelLocationUtils.getModelLocation(ModItems.remote, "_on"),
					TextureMapping.layer0(TextureMapping.getItemTexture(ModItems.remote, "_on")))));
	}

	private static void transceiverBlock(BlockModelGenerators generators, Block block)
	{
		Function<String, TextureMapping> textureMap = state -> new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "/side_" + state))
			.put(TextureSlot.END, TextureMapping.getBlockTexture(block, "/end_" + state));

		var off = ModelTemplates.CUBE_COLUMN.create(ModelLocationUtils.getModelLocation(block, "_off"), textureMap.apply("off"), generators.modelOutput);
		var on = ModelTemplates.CUBE_COLUMN.create(ModelLocationUtils.getModelLocation(block, "_on"), textureMap.apply("on"), generators.modelOutput);

		generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
			.with(BlockModelGenerators.createBooleanModelDispatch(POWERED, on, off)));
		generators.delegateItemModel(block, off);
	}

	private static void transceiverAttachmentBlock(BlockModelGenerators generators, Block block)
	{
		Function<String, TextureMapping> textureMap = state -> new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "/side_" + state))
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "/bottom_" + state))
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "/top_" + state));

		var base = new ModelTemplate(Optional.of(new ResourceLocation(WirelessRedstone.MOD_ID, "block/template/attachment")), Optional.empty(), TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
		var off = base.create(ModelLocationUtils.getModelLocation(block, "_off"), textureMap.apply("off"), generators.modelOutput);
		var on = base.create(ModelLocationUtils.getModelLocation(block, "_on"), textureMap.apply("on"), generators.modelOutput);

		generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
			.with(PropertyDispatch.property(POWERED)
				.select(false, Variant.variant().with(VariantProperties.MODEL, off))
				.select(true, Variant.variant().with(VariantProperties.MODEL, on)))
			.with(PropertyDispatch.properties(ATTACH_FACE, HORIZONTAL_FACING)
				.select(AttachFace.FLOOR, Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(AttachFace.FLOOR, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(AttachFace.FLOOR, Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(AttachFace.FLOOR, Direction.NORTH, Variant.variant())
				.select(AttachFace.CEILING, Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(AttachFace.CEILING, Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(AttachFace.CEILING, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
				.select(AttachFace.CEILING, Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
				.select(AttachFace.WALL, Direction.EAST,
					Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
				.select(AttachFace.WALL, Direction.WEST,
					Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
				.select(AttachFace.WALL, Direction.SOUTH,
					Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
				.select(AttachFace.WALL, Direction.NORTH,
					Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))));

		generators.delegateItemModel(block, off);
	}

	private static void p2pTransceiverBlock(BlockModelGenerators generators, Block block)
	{
		BiFunction<String, String, TextureMapping> textureMap = (linked, state) -> new TextureMapping()
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "/side_" + linked + "_" + state))
			.put(TextureSlot.END, TextureMapping.getBlockTexture(block, "/end_" + linked + "_" + state));

		var unlinkedOff = ModelTemplates.CUBE_COLUMN.create(ModelLocationUtils.getModelLocation(block, "_unlinked_off"), textureMap.apply("unlinked", "off"), generators.modelOutput);
		var unlinkedOn = ModelTemplates.CUBE_COLUMN.create(ModelLocationUtils.getModelLocation(block, "_unlinked_on"), textureMap.apply("unlinked", "on"), generators.modelOutput);
		var linkedOff = ModelTemplates.CUBE_COLUMN.create(ModelLocationUtils.getModelLocation(block, "_linked_off"), textureMap.apply("linked", "off"), generators.modelOutput);
		var linkedOn = ModelTemplates.CUBE_COLUMN.create(ModelLocationUtils.getModelLocation(block, "_linked_on"), textureMap.apply("linked", "on"), generators.modelOutput);

		generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
				.with(PropertyDispatch.properties(LINKED, POWERED)
					.select(false, false, Variant.variant().with(VariantProperties.MODEL, unlinkedOff))
					.select(false, true, Variant.variant().with(VariantProperties.MODEL, unlinkedOn))
					.select(true, false, Variant.variant().with(VariantProperties.MODEL, linkedOff))
					.select(true, true, Variant.variant().with(VariantProperties.MODEL, linkedOn))));

		generators.delegateItemModel(block, unlinkedOff);
	}

	private record ItemOverride(String key, float value, BiFunction<String, Float, Tuple<ResourceLocation, TextureMapping>> modelTexturesProvider) {}

	private static void registerOverrides(ItemModelGenerators generator, Item item, TextureMapping baseTextures, ItemOverride... overrides)
	{
		JsonArray array = new JsonArray();

		for (ItemOverride override : overrides)
		{
			var obj = new JsonObject();
			var predicate = new JsonObject();
			predicate.addProperty(override.key, override.value);
			obj.add("predicate", predicate);
			var modelTextures = override.modelTexturesProvider.apply(override.key, override.value);
			var model = ModelTemplates.FLAT_ITEM.create(modelTextures.getA(), modelTextures.getB(), generator.output);
			obj.addProperty("model", model.toString());
			array.add(obj);
		}

		ModelTemplates.FLAT_ITEM.create(
			ModelLocationUtils.getModelLocation(item), baseTextures,
			generator.output, (id, textures) ->
			{
				JsonObject jsonModel = ModelTemplates.FLAT_ITEM.createBaseTemplate(id, textures);
				jsonModel.add("overrides", array);
				return jsonModel;
			});
	}
}
