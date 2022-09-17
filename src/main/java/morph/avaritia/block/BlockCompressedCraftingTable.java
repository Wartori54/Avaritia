package morph.avaritia.block;

import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockCompressedCraftingTable extends CraftingTableBlock implements IModelRegister {
    private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.crafting");

    public BlockCompressedCraftingTable(BlockCompressedCraftingTable.Tier tier) {
        super(AbstractBlock.Properties.of(Material.WOOD).strength(4.0F * tier.getTier()).sound(SoundType.WOOD));
        setRegistryName(tier.getPrefixRegName() + "_crafting_table");
    }

//    @Override
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
//        if (world.isRemote) {
//            return true;
//        } else {
//            player.openGui(Avaritia.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
//            return true;
//        }
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerModels() {
//        ModelResourceLocation loc = new ModelResourceLocation("avaritia:crafting", "type=compressed");
//        ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                return loc;
//            }
//        });
//        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, loc);
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((p_220270_2_, p_220270_3_, p_220270_4_) ->
                new WorkbenchContainer(p_220270_2_, p_220270_3_, IWorldPosCallable.create(world, pos)) {
                    @Override
                    public boolean stillValid(PlayerEntity player) {
                        return this.access.evaluate((world, blockpos) -> {
                            boolean is_valid_block = world.getBlockState(blockpos).is(ModBlocks.compressed_crafting_table) ||
                                    world.getBlockState(blockpos).is(ModBlocks.double_compressed_craftingTable);

                            return is_valid_block && player.distanceToSqr((double) blockpos.getX() + 0.5D,
                                    (double) blockpos.getY() + 0.5D,
                                    (double) blockpos.getZ() + 0.5D)
                                    <= 64.0D;
                        }, true);
                    }
                },
                CONTAINER_TITLE);
    }

    public enum Tier {
        COMPRESSED(1, "compressed"),
        DOUBLE_COMPRESSED(2, "double_compressed");

        private final int tier;
        private final String prefixRegName;
        Tier(int tier, String prefixRegName) {
            this.tier = tier;
            this.prefixRegName = prefixRegName;
        }

        public int getTier() {
            return tier;
        }

        public String getPrefixRegName() {
            return prefixRegName;
        }
    }
}
