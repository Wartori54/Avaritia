package morph.avaritia;

import codechicken.lib.gui.SimpleItemGroup;
import morph.avaritia.init.ModItems;
import morph.avaritia.proxy.Proxy;
import morph.avaritia.proxy.ProxyClient;
import morph.avaritia.util.Lumberjack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;

import static morph.avaritia.Avaritia.*;

//@Mod (modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, acceptedMinecraftVersions = CodeChickenLib.MC_VERSION_DEP, dependencies = DEPENDENCIES)
@Mod(MOD_ID)
public class Avaritia {

    public static final String MOD_ID = "avaritia";
    public static final String MOD_NAME = "Avaritia";
    public static final String MOD_VERSION = "${mod_version}";
//    public static final String DEPENDENCIES = "" + CodeChickenLib.MOD_VERSION_DEP;

    public static final ItemGroup TAB = new SimpleItemGroup(MOD_ID, () -> new ItemStack(ModItems.infinity_catalyst));

    public static Avaritia instance;

//    @SidedProxy (clientSide = "morph.avaritia.proxy.ProxyClient", serverSide = "morph.avaritia.proxy.Proxy")
    public static Proxy proxy;

    public Avaritia() {
        if (instance != null) {
            Lumberjack.log(Level.WARN, "avaritia instance created multiple times");
        }
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> Proxy::new);
        proxy.onConstructor();
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        proxy.onCommonSetup(event);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        proxy.onClientSetup(event);
    }

    @SubscribeEvent
    public void onServerSetup(FMLDedicatedServerSetupEvent event) {
        proxy.onServerSetup(event);
    }

//    @Mod.EventHandler
//    public void preInit(FMLPreInitializationEvent event) {
//        MinecraftForge.EVENT_BUS.register(this);
//        proxy.preInit(event);
//        OreDictionary.registerOre("blockWool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
//        OreDictionary.registerOre("blockCrystalMatrix", new ItemStack(ModBlocks.resource, 1, 2));
//        OreDictionary.registerOre("blockCosmicNeutronium", new ItemStack(ModBlocks.resource, 1, 0));
//        OreDictionary.registerOre("blockInfinity", new ItemStack(ModBlocks.resource, 1, 1));
//        OreDictionary.registerOre("ingotCrystalMatrix", ModItems.crystal_matrix_ingot);
//        OreDictionary.registerOre("ingotCosmicNeutronium", ModItems.neutronium_ingot);
//        OreDictionary.registerOre("ingotInfinity", ModItems.infinity_ingot);
//        OreDictionary.registerOre("nuggetCosmicNeutronium", ModItems.neutron_nugget);
//    }
//
//    @Mod.EventHandler
//    public void init(FMLInitializationEvent event) {
//        proxy.init(event);
//    }
//
//    @Mod.EventHandler
//    public void postInit(FMLPostInitializationEvent event) {
//        proxy.postInit(event);
//    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<?> event) {
//        CompressorBalanceCalculator.gatherBalanceModifier();
//        AvaritiaRecipeManager.init();
//        FoodRecipes.initFoodRecipes();
    }

}
