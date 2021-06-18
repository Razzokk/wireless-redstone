package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;

public class BlockFrequency extends BlockRedstoneDevice
{
	private final Device.Type type;

	public BlockFrequency(Device.Type type)
	{
		super(Properties.of(Material.METAL));
		this.type = type;
	}

	public static void setPoweredState(World world, BlockPos pos, boolean powered)
	{
		BlockState state = world.getBlockState(pos);

		if (state.is(ModBlocks.redstoneReceiver) || state.is(ModBlocks.redstoneTransmitter))
			((BlockFrequency) state.getBlock()).setPowered(state, world, pos, powered);
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool)
	{
		return tool == ToolType.PICKAXE;
	}

	@Override
	public boolean isSignalSource(BlockState state)
	{
		return isReceiver();
	}

	@Override
	protected boolean isInputSide(BlockState state, Direction side)
	{
		return isTransmitter();
	}

	@Override
	protected boolean isOutputSide(BlockState state, Direction side)
	{
		return isReceiver();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
	{
		if (!world.isClientSide)
		{
			TileEntity tile = world.getBlockEntity(pos);

			if (tile instanceof TileFrequency)
			{
				TileFrequency tileFrequency = (TileFrequency) tile;
				tileFrequency.setFrequency((short) (tileFrequency.getFrequency() + 1));
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{
		if (!world.isClientSide)
		{
			RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) world);

			if (isTransmitter() && isGettingPowered(state, world, pos))
			{
				network.addDevice(Device.create((short) 0, type, pos));
				setPowered(state, world, pos, true);
			}
			else if (isReceiver())
			{
				network.addDevice(Device.create((short) 0, type, pos));
				setPowered(state, world, pos, network.isChannelActive((short) 0));
			}
		}
	}

	@Override
	protected void onInputChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbour, Direction side)
	{
		if (!world.isClientSide && isTransmitter() && shouldUpdate(state, world, pos))
		{
			boolean powered = isGettingPowered(state, world, pos);
			setPowered(state, world, pos, powered);
			TileEntity tile = world.getBlockEntity(pos);

			if (tile instanceof Device)
			{
				Device device = (Device) tile;
				RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) world);

				if (network != null)
				{
					if (powered)
						network.addDevice(device);
					else
						network.removeDevice(device);
				}
			}
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileFrequency(type);
	}

	public boolean isTransmitter()
	{
		return type == Device.Type.TRANSMITTER;
	}

	public boolean isReceiver()
	{
		return type == Device.Type.RECEIVER;
	}
}
