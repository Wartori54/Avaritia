package morph.avaritia.init;

import morph.avaritia.Avaritia;
import morph.avaritia.block.*;
import net.minecraft.block.Block;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Consumer;

@ObjectHolder(Avaritia.MOD_ID)
public class ModBlocks {

    @ObjectHolder("compressed_crafting_table") public static BlockCompressedCraftingTable compressed_crafting_table;
    @ObjectHolder("double_compressed_crafting_table") public static BlockCompressedCraftingTable double_compressed_craftingTable;
    @ObjectHolder("extreme_crafting_table") public static BlockExtremeCraftingTable extreme_crafting_table;

    @ObjectHolder("neutron_block") public static Block neutron_block;
    @ObjectHolder("infinity_block") public static Block infinity_block;
    @ObjectHolder("crystal_matrix") public static Block crystal_matrix;

    @ObjectHolder("neutron_collector") public static BlockNeutronCollector neutron_collector;
    @ObjectHolder("neutronium_compressor") public static BlockNeutroniumCompressor neutronium_compressor;

    public static void init(RegistryEvent.Register<Block> event) {
        BlockRegistry registry = new BlockRegistry(event);

        registry.registerBlockResource("neutron_block");
        registry.registerBlockResource("infinity_block");
        registry.registerBlockResource("crystal_matrix");

        registry.registerBlockNoReg(new BlockCompressedCraftingTable(BlockCompressedCraftingTable.Tier.COMPRESSED));
        registry.registerBlockNoReg(new BlockCompressedCraftingTable(BlockCompressedCraftingTable.Tier.DOUBLE_COMPRESSED));

        registry.registerBlock(new BlockExtremeCraftingTable(), "extreme_crafting_table");

        registry.registerBlock(new BlockNeutronCollector(), "neutron_collector");

        registry.registerBlock(new BlockNeutroniumCompressor(), "neutronium_compressor");
    }

    private static class BlockRegistry {
        private final RegistryEvent.Register<Block> event;

        private BlockRegistry(RegistryEvent.Register<Block> event) {
            this.event = event;
        }

        public void registerBlockResource(String name) {
            event.getRegistry().register(new BlockResource(new ResourceLocation(Avaritia.MOD_ID, name)));
        }

        public void registerBlock(Block block, String name) {
            event.getRegistry().register(block.setRegistryName(new ResourceLocation(Avaritia.MOD_ID, name)));

        }

        public void registerBlockNoReg(Block block) {
            event.getRegistry().register(block);
        }
    }

//    public static <V extends Block> V registerBlock(V block) {
//        registerImpl(block, ForgeRegistries.BLOCKS::register);
//        return block;
//    }
//
//    public static <V extends Item> V registerItem(V item) {
//        registerImpl(item, ForgeRegistries.ITEMS::register);
//        return item;
//    }
//
//    public static <V extends IForgeRegistryEntry<V>> V registerImpl(V registryObject, Consumer<V> registerCallback) {
//        registerCallback.accept(registryObject);
//
//        if (registryObject instanceof IModelRegister) {
//            Avaritia.proxy.addModelRegister((IModelRegister) registryObject);
//        }
//
//        return registryObject;
//    }
//
//    public static ItemBlock registerItemBlock(Block block) {
//        ItemBlock itemBlock = new ItemBlock(block);
//        registerItem(itemBlock.setRegistryName(block.getRegistryName()));
//        return itemBlock;
//    }

}
