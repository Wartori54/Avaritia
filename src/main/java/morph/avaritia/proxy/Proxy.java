package morph.avaritia.proxy;

import codechicken.lib.config.StandardConfigFile;
import com.mojang.authlib.GameProfile;
import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.entity.EntityGapingVoid;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.entity.EntityHeavenSubArrow;
import morph.avaritia.handler.AbilityHandler;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.handler.ConfigHandler;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModContent;
import morph.avaritia.init.ModEntities;
import morph.avaritia.init.ModItems;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.util.Lumberjack;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import org.apache.logging.log4j.Level;

import java.nio.file.Paths;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Avaritia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Proxy {

    public static final GameProfile avaritiaFakePlayer = new GameProfile(UUID.fromString("32283731-bbef-487c-bb69-c7e32f84ed27"), "[Avaritia]");
    public static boolean flag = true;

    public void onConstructor() {
        ConfigHandler.init(new StandardConfigFile(Paths.get("./config/avaritia.cfg")));
//        NetworkRegistry.INSTANCE.registerGuiHandler(Avaritia.instance, new GUIHandler());
        MinecraftForge.EVENT_BUS.register(new AbilityHandler());
        MinecraftForge.EVENT_BUS.register(new AvaritiaEventHandler());
        MinecraftForge.EVENT_BUS.register(new AvaritiaRecipeManager());

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.init(event);
        flag = false;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.init(event);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModEntities.init(event);
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        ModContent.registerTileEntities(event);
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        ModContent.registerContainers(event);
    }

    @SubscribeEvent
    public static void registerLoots(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        ModContent.registerLoots(event);
    }



    public void onCommonSetup(FMLCommonSetupEvent event) {

    }

    public void onClientSetup(FMLClientSetupEvent event) {

    }

    public void onServerSetup(FMLDedicatedServerSetupEvent event) {

    }



//    @SubscribeEvent
//    public static void onReloadEvent(AddReloadListenerEvent event) {
//        event.addListener();
//    }

//    public void preInit(FMLPreInitializationEvent event) {
//        ConfigHandler.init(event.getSuggestedConfigurationFile());
//        ModItems.init();
//        ModBlocks.init();
//        NetworkRegistry.INSTANCE.registerGuiHandler(Avaritia.instance, new GUIHandler());
//        MinecraftForge.EVENT_BUS.register(new AbilityHandler());
//        MinecraftForge.EVENT_BUS.register(new AvaritiaEventHandler());
//
//        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:endest_pearl"), EntityEndestPearl.class, "EndestPearl", 1, Avaritia.instance, 64, 10, true);
//        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:gaping_void"), EntityGapingVoid.class, "GapingVoid", 2, Avaritia.instance, 256, 10, false);
//        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:heaven_arrow"), EntityHeavenArrow.class, "HeavenArrow", 3, Avaritia.instance, 32, 1, true);
//        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:heaven_sub_arrow"), EntityHeavenSubArrow.class, "HeavenSubArrow", 4, Avaritia.instance, 32, 2, true);
//    }
//
//    public void init(FMLInitializationEvent event) {
//    }
//
//    public void postInit(FMLPostInitializationEvent event) {
//
//    }

    public void addModelRegister(IModelRegister register) {

    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return true;
    }

    public World getClientWorld() {
        return null;
    }

}
