package morph.avaritia.block;

import morph.avaritia.api.registration.IModelRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

/**
 * Created by covers1624 on 11/04/2017.
 */
public class BlockResource extends Block implements IModelRegister {

//    public static final PropertyEnum<BlockResource.Type> VARIANT = PropertyEnum.create("type", BlockResource.Type.class);

    public BlockResource(ResourceLocation name) {
        super(AbstractBlock.Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(3)
                .strength(50, 2000));
        setRegistryName(name);
//        registerDefaultState(this.getStateDefinition().any());
    }

//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, VARIANT);
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
//        //TODO
//        for (int i = 0; i < Type.METADATA_LOOKUP.length; i++) {
//            list.add(new ItemStack(this, 1, i));
//        }
//
//    }

//    @Override
//    public int damageDropped(IBlockState state) {
//        return state.getValue(VARIANT).getMetadata();
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerModels() {
//        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(ModBlocks.resource), stack -> new ModelResourceLocation("avaritia:block_resource", "type=" + BlockResource.Type.byMetadata(stack.getMetadata()).getName()));
    }

}
