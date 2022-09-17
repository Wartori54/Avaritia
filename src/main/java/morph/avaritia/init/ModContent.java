package morph.avaritia.init;

import morph.avaritia.Avaritia;
import morph.avaritia.container.ContainerExtremeCrafting;
import morph.avaritia.container.ContainerNeutronCollector;
import morph.avaritia.container.ContainerNeutroniumCompressor;
import morph.avaritia.tile.TileDireCraftingTable;
import morph.avaritia.tile.TileNeutronCollector;
import morph.avaritia.tile.TileNeutroniumCompressor;
import morph.avaritia.util.MatterClusterModifier;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

// misc content that is not items nor blocks
@ObjectHolder(Avaritia.MOD_ID)
public class ModContent {

    @ObjectHolder("extreme_crafting_table") public static TileEntityType<TileDireCraftingTable> tileDireCraftingTable;
    @ObjectHolder("neutron_collector") public static TileEntityType<TileNeutronCollector> tileNeutronCollector;
    @ObjectHolder("neutronium_compressor") public static TileEntityType<TileNeutroniumCompressor> tileNeutroniumCompressor;

    @ObjectHolder("extreme_crafting_table") public static ContainerType<ContainerExtremeCrafting> containerExtremeCrafting;
    @ObjectHolder("neutron_collector") public static ContainerType<ContainerNeutronCollector> containerNeutronCollector;
    @ObjectHolder("neutronium_compressor") public static ContainerType<ContainerNeutroniumCompressor> containerNeutroniumCompressor;

    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(
                TileEntityType.Builder.of(TileDireCraftingTable::new, ModBlocks.extreme_crafting_table)
                .build(null)
                .setRegistryName("extreme_crafting_table"));
        event.getRegistry().register(
                TileEntityType.Builder.of(TileNeutronCollector::new, ModBlocks.neutron_collector)
                        .build(null)
                        .setRegistryName("neutron_collector"));
        event.getRegistry().register(
                TileEntityType.Builder.of(TileNeutroniumCompressor::new, ModBlocks.neutronium_compressor)
                        .build(null)
                        .setRegistryName("neutronium_compressor"));
    }

    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create(ContainerExtremeCrafting::new).setRegistryName("extreme_crafting_table"));
        event.getRegistry().register(IForgeContainerType.create(ContainerNeutronCollector::new).setRegistryName("neutron_collector"));
        event.getRegistry().register(IForgeContainerType.create(ContainerNeutroniumCompressor::new).setRegistryName("neutronium_compressor"));
    }

    public static void registerLoots(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new MatterClusterModifier.Serializer().setRegistryName("matter_cluster_modifier"));
    }

//    @SubscribeEvent
//    public static void onReloadEvent(AddReloadListenerEvent event) {
//        event.addListener();
//    }
}
