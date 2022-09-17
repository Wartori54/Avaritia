package morph.avaritia.block;

import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.tile.TileDireCraftingTable;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockExtremeCraftingTable extends ContainerBlock implements IModelRegister {

    public BlockExtremeCraftingTable() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .sound(SoundType.GLASS)
                .requiresCorrectToolForDrops()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(3)
                .strength(50, 2000));
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
        return new TileDireCraftingTable();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerModels() {
//        ModelResourceLocation loc = new ModelResourceLocation("avaritia:crafting", "type=extreme");
//        ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                return loc;
//            }
//        });
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, loc);
    }

    @Override
    public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.025F);
        stack = getItemBlockWithNBT(te);
        popResource(worldIn, pos, stack);
    }

    private ItemStack getItemBlockWithNBT(@Nullable TileEntity te) {
        ItemStack stack = new ItemStack(this);
        CompoundNBT nbttagcompound = new CompoundNBT();
        if (te != null) {
            te.save(nbttagcompound);
            stack.addTagElement("BlockEntityTag", nbttagcompound);
        }
        return stack;
    }
}
