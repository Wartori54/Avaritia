package morph.avaritia.block;

import codechicken.lib.util.ItemUtils;
import codechicken.lib.util.RotationUtils;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.tile.TileNeutronCollector;
import morph.avaritia.util.ModHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockNeutronCollector extends ContainerBlock implements IModelRegister {

    public BlockNeutronCollector() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(3)
                .strength(20));

        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                        .setValue(ModHelper.ACTIVE, false)
        );
    }


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING).add(ModHelper.ACTIVE);
    }

//    @Override // TODO: confirm this is metadata related
//    public BlockState getActualState(BlockState state, BlockState worldIn, BlockPos pos) {
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        if (tileEntity instanceof TileMachineBase) {
//            TileMachineBase machineBase = (TileMachineBase) tileEntity;
//            state = state.withProperty(AvaritiaProps.HORIZONTAL_FACING, machineBase.getFacing());
//            state = state.withProperty(AvaritiaProps.ACTIVE, machineBase.isActive());
//        }
//        return super.getActualState(state, worldIn, pos);
//    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        } else {
            player.openMenu(blockState.getMenuProvider(world, blockPos));
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new TileNeutronCollector();
    }

    @Override // TODO: check if correct
    public void onRemove(BlockState oldState, World world, BlockPos blockPos, BlockState newState, boolean bool) {
        TileNeutronCollector collector = (TileNeutronCollector) world.getBlockEntity(blockPos);

        if (collector != null) {
            ItemUtils.dropInventory(world, blockPos, collector);
        }

        super.onRemove(oldState, world, blockPos, newState, bool);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerModels() {
//        ResourceLocation location = new ResourceLocation("avaritia:machine");
//        ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                String modelLoc = "type=neutron_collector";
//                modelLoc += ",facing=" + state.getValue(AvaritiaProps.HORIZONTAL_FACING).getName();
//                modelLoc += ",active=" + state.getValue(AvaritiaProps.ACTIVE).toString().toLowerCase();
//                return new ModelResourceLocation(location, modelLoc);
//            }
//        });
//        ModelResourceLocation invLoc = new ModelResourceLocation(location, "type=neutron_collector,facing=north,active=true");
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, invLoc);
//        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), stack -> invLoc);
    }
}
