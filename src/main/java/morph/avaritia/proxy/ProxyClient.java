package morph.avaritia.proxy;

import codechicken.lib.texture.SpriteRegistryHelper;
import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.AvaritiaClientEventHandler;
import morph.avaritia.client.gui.GUIExtremeCrafting;
import morph.avaritia.client.gui.GUINeutronCollector;
import morph.avaritia.client.gui.GUINeutroniumCompressor;
import morph.avaritia.client.render.shader.ShaderHelper;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModContent;
import morph.avaritia.init.ModItems;
import morph.avaritia.network.NetworkDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

import java.util.HashSet;
import java.util.Set;

public class ProxyClient extends Proxy {

    private Set<IModelRegister> modelRegisters = new HashSet<>();

    public static final SpriteRegistryHelper spriteHelper = new SpriteRegistryHelper();

    //@formatter:off
    public static final int[] SINGULARITY_COLOURS_FOREGROUND = new int[] {
            0xE6E7E8, 0xE8EF23, 0x5a82e2, 0xDF0000,
            0xffffff, 0xE47200, 0xA5C7DE, 0x444072,
            0xC0C0C0, 0xDEE187, 0x45ACA5, 0x5CBE34,
            0xD62306, 0x00BFFF, 0xE6E6FA
    };

    public static final int[] SINGULARITY_COLOURS_BACKGROUND = new int[] {
            0x7F7F7F, 0xdba213, 0x224baf, 0x900000,
            0x94867d, 0x89511A, 0x9BA9B2, 0x3E3D4E,
            0xD5D5D5, 0xC4C698, 0x8fcdc9, 0x8cd170,
            0xfffc95, 0x5a82e2, 0xE6E6FA
    };

    public static final int[][] SINGULARITY_COLOURS = new int[][] {
            SINGULARITY_COLOURS_FOREGROUND,
            SINGULARITY_COLOURS_BACKGROUND
    };
    //@formatter:on

    public void onConstructor() {
        super.onConstructor();
        spriteHelper.addIIconRegister(new AvaritiaTextures());
        MinecraftForge.EVENT_BUS.register(new AvaritiaClientEventHandler());


//        {
//            ModelResourceLocation pickaxe = new ModelResourceLocation(tools, "infinity_pickaxe=pickaxe");
//            ModelResourceLocation hammer = new ModelResourceLocation(tools, "infinity_pickaxe=hammer");
//            ModelLoader.registerItemVariants(ModItems.infinity_pickaxe, pickaxe, hammer);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_pickaxe, stack -> {
//                if (stack.hasTagCompound()) {
//                    if (ItemNBTUtils.getBoolean(stack, "hammer")) {
//                        return hammer;
//                    }
//                }
//                return pickaxe;
//            });
//        }
//
//        {
//            ModelResourceLocation shovel = new ModelResourceLocation(tools, "infinity_shovel=shovel");
//            ModelResourceLocation destroyer = new ModelResourceLocation(tools, "infinity_shovel=destroyer");
//            ModelLoader.registerItemVariants(ModItems.infinity_shovel, shovel, destroyer);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_shovel, stack -> {
//                if (stack.hasTagCompound()) {
//                    if (ItemNBTUtils.getBoolean(stack, "destroyer")) {
//                        return destroyer;
//                    }
//                }
//                return shovel;
//            });
//        }
//
//        {
//            ModelResourceLocation axe = new ModelResourceLocation(tools, "type=infinity_axe");
//            ModelLoader.registerItemVariants(ModItems.infinity_axe, axe);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_axe, (ItemStack stack) -> axe);
//        }
//
//        {
//            ModelResourceLocation hoe = new ModelResourceLocation(tools, "type=infinity_hoe");
//            ModelLoader.registerItemVariants(ModItems.infinity_axe, hoe);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_hoe, (ItemStack stack) -> hoe);
//        }
//
//        {
//            ModelResourceLocation helmet = new ModelResourceLocation(tools, "armor=helmet");
//            ModelLoader.registerItemVariants(ModItems.infinity_helmet, helmet);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_helmet, (ItemStack stack) -> helmet);
//        }
//
//        {
//            ModelResourceLocation chestplate = new ModelResourceLocation(tools, "armor=chestplate");
//            ModelLoader.registerItemVariants(ModItems.infinity_chestplate, chestplate);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_chestplate, (ItemStack stack) -> chestplate);
//        }
//
//        {
//            ModelResourceLocation legs = new ModelResourceLocation(tools, "armor=legs");
//            ModelLoader.registerItemVariants(ModItems.infinity_pants, legs);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_pants, (ItemStack stack) -> legs);
//        }
//
//        {
//            ModelResourceLocation boots = new ModelResourceLocation(tools, "armor=boots");
//            ModelLoader.registerItemVariants(ModItems.infinity_boots, boots);
//            ModelLoader.setCustomMeshDefinition(ModItems.infinity_boots, (ItemStack stack) -> boots);
//        }
//
//        {
//            ModelResourceLocation sword = new ModelResourceLocation(tools, "type=skull_sword");
//            ModelLoader.registerItemVariants(ModItems.skull_sword, sword);
//            ModelLoader.setCustomMeshDefinition(ModItems.skull_sword, (ItemStack stack) -> sword);
//        }
//
//        {
//            ModelResourceLocation stew = new ModelResourceLocation(resource, "type=ultimate_stew");
//            ModelLoader.registerItemVariants(ModItems.ultimate_stew, stew);
//            ModelLoader.setCustomMeshDefinition(ModItems.ultimate_stew, (ItemStack stack) -> stew);
//        }
//
//        {
//            ModelResourceLocation meatballs = new ModelResourceLocation(resource, "type=cosmic_meatballs");
//            ModelLoader.registerItemVariants(ModItems.cosmic_meatballs, meatballs);
//            ModelLoader.setCustomMeshDefinition(ModItems.cosmic_meatballs, (ItemStack stack) -> meatballs);
//        }
//
//        {
//            ModelResourceLocation empty = new ModelResourceLocation(resource, "matter_cluster=empty");
//            ModelResourceLocation full = new ModelResourceLocation(resource, "matter_cluster=full");
//            ModelLoader.registerItemVariants(ModItems.matter_cluster, empty, full);
//            ModelLoader.setCustomMeshDefinition(ModItems.matter_cluster, (ItemStack stack) -> {
//                if (ItemMatterCluster.getClusterSize(stack) == ItemMatterCluster.CAPACITY) {
//                    return full;
//                }
//                return empty;
//            });
//        }
        NetworkDispatcher.init();
    }

    @SuppressWarnings ("unchecked")
//    @Override
//    public void postInit(FMLPostInitializationEvent event) {
//        super.postInit(event);
//
//        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
//        itemColors.registerItemColorHandler((stack, tintIndex) -> SINGULARITY_COLOURS[tintIndex ^ 1][stack.getItemDamage()], ModItems.singularity);
//
//        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
//
//        Render<EntityItem> render = (Render<EntityItem>) manager.entityRenderMap.get(EntityItem.class);
//        if (render == null) {
//            throw new RuntimeException("EntityItem does not have a Render bound... This is likely a bug..");
//        }
//        manager.entityRenderMap.put(EntityItem.class, new WrappedEntityItemRenderer(manager, render));
//    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        super.onCommonSetup(event);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        super.onClientSetup(event);
        registerRenderers();
        ItemColors itemColors = Minecraft.getInstance().getItemColors();
        itemColors.register((stack, tintIndex) -> /*SINGULARITY_COLOURS[tintIndex ^ 1][stack.getTag().getInt("id")]*/0xffffff, ModItems.singularity);
        ItemModelsProperties.register(ModItems.infinity_bow, new ResourceLocation("pull"), (p_239429_0_, p_239429_1_, p_239429_2_) -> {
            if (p_239429_2_ == null) {
                return 0.0F;
            } else {
                return p_239429_2_.getUseItem() != p_239429_0_ ? 0.0F : (float)(p_239429_0_.getUseDuration() - p_239429_2_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemModelsProperties.register(ModItems.infinity_bow, new ResourceLocation("pulling"), (p_239428_0_, p_239428_1_, p_239428_2_) ->
                p_239428_2_ != null && p_239428_2_.isUsingItem() && p_239428_2_.getUseItem() == p_239428_0_ ? 1.0F : 0.0F);
        ItemModelsProperties.register(ModItems.infinity_pickaxe, new ResourceLocation(Avaritia.MOD_ID, "is_hammer"), (stack, world, player) ->
                player != null && stack.getTag() != null && stack.getTag().getBoolean("hammer") ? 1F : 0F
        );
        ItemModelsProperties.register(ModItems.infinity_shovel, new ResourceLocation(Avaritia.MOD_ID, "is_destroyer"), (stack, world, player) ->
                player != null && stack.getTag() != null && stack.getTag().getBoolean("destroyer") ? 1F : 0F
        );
//        RenderManager manager = Minecraft.getInstance().g();
//
//        Render<EntityItem> render = (Render<EntityItem>) manager.entityRenderMap.get(EntityItem.class);
//        if (render == null) {
//            throw new RuntimeException("EntityItem does not have a Render bound... This is likely a bug..");
//        }
//        manager.entityRenderMap.put(EntityItem.class, new WrappedEntityItemRenderer(manager, render));
        registerGuiHandler();
//        for (IModelRegister register : modelRegisters) {
//            register.registerModels();
//        }
//        ShaderHelper.initShaders();
        ResourceLocation tools = new ResourceLocation("avaritia:tools");
        ResourceLocation resource = new ResourceLocation("avaritia:resource");

    }

    public void onServerSetup(FMLDedicatedServerSetupEvent event) {
        super.onServerSetup(event);
    }

    public static void registerGuiHandler() {
        ScreenManager.register(ModContent.containerExtremeCrafting, GUIExtremeCrafting::new);
        ScreenManager.register(ModContent.containerNeutronCollector, GUINeutronCollector::new);
        ScreenManager.register(ModContent.containerNeutroniumCompressor, GUINeutroniumCompressor::new);
    }

    @Override
    public void addModelRegister(IModelRegister register) {
        modelRegisters.add(register);
    }

    private void registerRenderers() {
//        RenderingRegistry.registerEntityRenderingHandler(EntityEndestPearl.class, manager -> new RenderSnowball<>(manager, ModItems.endest_pearl, Minecraft.getMinecraft().getRenderItem()));
//        RenderingRegistry.registerEntityRenderingHandler(EntityGapingVoid.class, RenderGapingVoid::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityHeavenArrow.class, RenderHeavenArrow::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityHeavenSubArrow.class, RenderHeavenArrow::new);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().level;
    }
}
