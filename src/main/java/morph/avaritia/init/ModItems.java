package morph.avaritia.init;

import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.item.*;
import morph.avaritia.item.tools.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;

import net.minecraftforge.registries.ObjectHolder;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by covers1624 on 11/04/2017.
 */

@ObjectHolder(Avaritia.MOD_ID)
public class ModItems {

    public static Rarity COSMIC_RARITY = Rarity.create("COSMIC", TextFormatting.RED);

//    public static ItemResource resource;

    //@formatter:off
    // the old avaritia:resource
    @ObjectHolder("diamond_lattice") public static Item diamond_lattice;
    @ObjectHolder("crystal_matrix_ingot") public static Item crystal_matrix_ingot;
    @ObjectHolder("neutron_pile") public static Item neutron_pile;
    @ObjectHolder("neutron_nugget") public static Item neutron_nugget;
    @ObjectHolder("neutronium_ingot") public static Item neutron_ingot; // TODO check if old ingots get converted to new ones
    @ObjectHolder("infinity_catalyst") public static Item infinity_catalyst;
    @ObjectHolder("infinity_ingot") public static Item infinity_ingot;
    @ObjectHolder("record_fragment") public static Item record_fragment;
    //@formatter:on

    /**
     * 0 = Iron,
     * 1 = Gold,
     * 2 = Lapis,
     * 3 = Redstone,
     * 4 = Nether Quartz,
     * 5 = Copper,
     * 6 = Tin,
     * 7 = Lead,
     * 8 = Silver,
     * 9 = Nickel,
     * 10 = Diamond,
     * 11 = Emerald
     * 12 = Fluxed
     */
    @ObjectHolder("singularity")
    public static ItemSingularity singularity;

    @ObjectHolder("infinity_sword") public static ItemSwordInfinity infinity_sword;
    @ObjectHolder("infinity_bow") public static ItemBowInfinity infinity_bow;
    @ObjectHolder("infinity_pickaxe") public static ItemPickaxeInfinity infinity_pickaxe;
    @ObjectHolder("infinity_shovel") public static ItemShovelInfinity infinity_shovel;
    @ObjectHolder("infinity_axe") public static ItemAxeInfinity infinity_axe;
    @ObjectHolder("infinity_hoe") public static ItemHoeInfinity infinity_hoe;

    @ObjectHolder("infinity_helmet") public static ItemArmorInfinity infinity_helmet;
    @ObjectHolder("infinity_chestplate") public static ItemArmorInfinity infinity_chestplate;
    @ObjectHolder("infinity_pants") public static ItemArmorInfinity infinity_pants;
    @ObjectHolder("infinity_boots") public static ItemArmorInfinity infinity_boots;

    @ObjectHolder("skullfire_sword") public static ItemSwordSkulls skull_sword;

    @ObjectHolder("endest_pearl") public static ItemEndestPearl endest_pearl;

    @ObjectHolder("ultimate_stew") public static Item ultimate_stew;
    @ObjectHolder("cosmic_meatballs") public static Item cosmic_meatballs;

    public static Food ultimate_stew_food = new Food.Builder()
            .nutrition(20)
            .saturationMod(20)
            .effect(() -> new EffectInstance(Effects.REGENERATION, 300, 1), 1F)
            .build();
    public static Food cosmic_meatballs_food = new Food.Builder()
            .nutrition(20)
            .saturationMod(20)
            .effect(() -> new EffectInstance(Effects.DAMAGE_BOOST, 300, 1), 1F)
            .build();
//    public static ItemFracturedOre fractured_ore;

    @ObjectHolder("matter_cluster") public static ItemMatterCluster matter_cluster;

//    public static ItemStack ironSingularity;
//    public static ItemStack goldSingularity;
//    public static ItemStack lapisSingularity;
//    public static ItemStack redstoneSingularity;
//    public static ItemStack quartzSingularity;
//    public static ItemStack copperSingularity;
//    public static ItemStack tinSingularity;
//    public static ItemStack leadSingularity;
//    public static ItemStack silverSingularity;
//    public static ItemStack nickelSingularity;
//    public static ItemStack diamondSingularity;
//    public static ItemStack emeraldSingularity;
//    public static ItemStack fluxedSingularity;
//    public static ItemStack platinumSingularity;
//    public static ItemStack iridiumSingularity;


    public static void init(RegistryEvent.Register<Item> event) {
        ItemRegistry registry = new ItemRegistry(event);
        // resources
        registry.registerItemResource("diamond_lattice", Rarity.UNCOMMON);
        registry.registerItemResource("crystal_matrix_ingot", Rarity.RARE);
        registry.registerItemResource("neutron_pile", Rarity.UNCOMMON);
        registry.registerItemResource("neutron_nugget", Rarity.UNCOMMON);
        registry.registerItemResource("neutronium_ingot", Rarity.RARE);
        registry.registerItemResource("infinity_catalyst", Rarity.EPIC);
        registry.registerItemResource("infinity_ingot", COSMIC_RARITY);
        registry.registerItemResource("record_fragment", Rarity.UNCOMMON);

        // block items
        registry.registerBlockItem(ModBlocks.compressed_crafting_table);
        registry.registerBlockItem(ModBlocks.double_compressed_craftingTable);
        registry.registerBlockItem(ModBlocks.extreme_crafting_table);
        registry.registerBlockItem(ModBlocks.neutron_block);
        registry.registerBlockItem(ModBlocks.infinity_block);
        registry.registerBlockItem(ModBlocks.crystal_matrix);
        registry.registerBlockItem(ModBlocks.neutron_collector);
        registry.registerBlockItem(ModBlocks.neutronium_compressor);

        // TODO: singularity registration
        registry.registerItem(new ItemSingularity(), "singularity");

        registry.registerItem(new ItemSwordInfinity(), "infinity_sword");

        registry.registerItem(new ItemBowInfinity(), "infinity_bow");

        registry.registerItem(new ItemPickaxeInfinity(), "infinity_pickaxe");

        registry.registerItem(new ItemShovelInfinity(), "infinity_shovel");

        registry.registerItem(new ItemAxeInfinity(), "infinity_axe");

        registry.registerItem(new ItemHoeInfinity(), "infinity_hoe");

        registry.registerItem(new ItemSwordSkulls(), "skullfire_sword");

        registry.registerItem(new ItemArmorInfinity(EquipmentSlotType.HEAD), "infinity_helmet");
        registry.registerItem(new ItemArmorInfinity(EquipmentSlotType.CHEST), "infinity_chestplate");
        registry.registerItem(new ItemArmorInfinity(EquipmentSlotType.LEGS), "infinity_pants");
        registry.registerItem(new ItemArmorInfinity(EquipmentSlotType.FEET), "infinity_boots");

        registry.registerItem(new ItemEndestPearl(), "endest_pearl");

        registry.registerItem(new ItemMatterCluster(), "matter_cluster");

        registry.registerItem(new Item(new Item.Properties().tab(Avaritia.TAB).food(ultimate_stew_food)), "ultimate_stew");
        registry.registerItem(new Item(new Item.Properties().tab(Avaritia.TAB).food(cosmic_meatballs_food)), "cosmic_meatballs");


    }

    private static class ItemRegistry {
        private final RegistryEvent.Register<Item> event;
        public ItemRegistry(RegistryEvent.Register<Item> event) {
            this.event = event;
        }
        public void registerItemResource(String name, Rarity rarity) {
            Item item = new ItemResource(new ResourceLocation(Avaritia.MOD_ID, name), rarity);
            this.event.getRegistry().register(item);
            Avaritia.proxy.addModelRegister((IModelRegister) item);
        }

        public void registerItem(Item item, String name) {
            this.event.getRegistry().register(item.setRegistryName(new ResourceLocation(Avaritia.MOD_ID, name)));
            if (item instanceof IModelRegister) {
                Avaritia.proxy.addModelRegister((IModelRegister) item);
            }
        }

        public void registerBlockItem(Block block) {
            this.event.getRegistry().register(new BlockItem(block, new Item.Properties().tab(Avaritia.TAB))
                    .setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

//    public static <V extends IForgeRegistryEntry<V>> V registerImpl(V registryObject, Consumer<V> registerCallback) {
//        registerCallback.accept(registryObject);
//
//        if (registryObject instanceof IModelRegister) {
//            Avaritia.proxy.addModelRegister((IModelRegister) registryObject);
//        }
//
//        return registryObject;
//    }

}
