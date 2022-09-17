package morph.avaritia.init;

import codechicken.lib.texture.AtlasRegistrar;
import codechicken.lib.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

/**
 * Created by covers1624 on 17/04/2017.
 */
public class AvaritiaTextures implements IIconRegister {

    @Override
    public void registerIcons(AtlasRegistrar textureMap) {
        map = textureMap;

        //@formatter:off
        register(ITEMS_ + "halo", tex -> HALO = tex);
        register(ITEMS_ + "halo_noise", tex -> HALO_NOISE = tex);

        register(MODELS_ + "infinity_armor_mask", tex -> INFINITY_ARMOR_MASK = tex);
        register(MODELS_ + "infinity_armor_mask_inv", tex -> INFINITY_ARMOR_MASK_INV = tex);
        register(MODELS_ + "infinity_armor_mask_wings", tex -> INFINITY_ARMOR_MASK_WINGS = tex);

        register(SHADER_ + "cosmic_0", tex -> COSMIC_0 = tex);
        register(SHADER_ + "cosmic_1", tex -> COSMIC_1 = tex);
        register(SHADER_ + "cosmic_2", tex -> COSMIC_2 = tex);
        register(SHADER_ + "cosmic_3", tex -> COSMIC_3 = tex);
        register(SHADER_ + "cosmic_4", tex -> COSMIC_4 = tex);
        register(SHADER_ + "cosmic_5", tex -> COSMIC_5 = tex);
        register(SHADER_ + "cosmic_6", tex -> COSMIC_6 = tex);
        register(SHADER_ + "cosmic_7", tex -> COSMIC_7 = tex);
        register(SHADER_ + "cosmic_8", tex -> COSMIC_8 = tex);
        register(SHADER_ + "cosmic_9", tex -> COSMIC_9 = tex);

        register(TOOLS_ + "infinity_sword/mask", tex -> INFINITY_SWORD_MASK = tex);

        register(TOOLS_ + "infinity_bow/idle", tex -> INFINITY_BOW_IDLE = tex);
        register(TOOLS_ + "infinity_bow/pull_0", tex -> INFINITY_BOW_PULL_0 = tex);
        register(TOOLS_ + "infinity_bow/pull_1", tex -> INFINITY_BOW_PULL_1 = tex);
        register(TOOLS_ + "infinity_bow/pull_2", tex -> INFINITY_BOW_PULL_2 = tex);

        register(TOOLS_ + "infinity_bow/idle_mask", tex -> INFINITY_BOW_IDLE_MASK = tex);
        register(TOOLS_ + "infinity_bow/pull_0_mask", tex -> INFINITY_BOW_PULL_0_MASK = tex);
        register(TOOLS_ + "infinity_bow/pull_1_mask", tex -> INFINITY_BOW_PULL_1_MASK = tex);
        register(TOOLS_ + "infinity_bow/pull_2_mask", tex -> INFINITY_BOW_PULL_2_MASK = tex);


        INFINITY_BOW_PULL = new TextureAtlasSprite[] {
                INFINITY_BOW_PULL_0,
                INFINITY_BOW_PULL_1,
                INFINITY_BOW_PULL_2
        };

        INFINITY_BOW_PULL_MASK = new TextureAtlasSprite[] {
                INFINITY_BOW_PULL_0_MASK,
                INFINITY_BOW_PULL_1_MASK,
                INFINITY_BOW_PULL_2_MASK
        };


        COSMIC = new TextureAtlasSprite[] {
                COSMIC_0,
                COSMIC_1,
                COSMIC_2,
                COSMIC_3,
                COSMIC_4,
                COSMIC_5,
                COSMIC_6,
                COSMIC_7,
                COSMIC_8,
                COSMIC_9
        };
        //@formatter:on

    }

    // Bouncer to make the class readable.
    private static void register(String sprite, Consumer<TextureAtlasSprite> consumer) {
        map.registerSprite(new ResourceLocation(sprite), consumer);
    }

    //Assign the TextureMap to a file to make things even more readable!.
    private static AtlasRegistrar map;

    private static final String ITEMS_ = "avaritia:items/";
    private static final String MODELS_ = "avaritia:models/";
    private static final String SHADER_ = "avaritia:shader/";
    private static final String TOOLS_ = ITEMS_ + "tools/";

    public static TextureAtlasSprite HALO;
    public static TextureAtlasSprite HALO_NOISE;

    public static TextureAtlasSprite INFINITY_ARMOR_MASK;
    public static TextureAtlasSprite INFINITY_ARMOR_MASK_INV;
    public static TextureAtlasSprite INFINITY_ARMOR_MASK_WINGS;

    public static TextureAtlasSprite[] COSMIC;
    public static TextureAtlasSprite COSMIC_0;
    public static TextureAtlasSprite COSMIC_1;
    public static TextureAtlasSprite COSMIC_2;
    public static TextureAtlasSprite COSMIC_3;
    public static TextureAtlasSprite COSMIC_4;
    public static TextureAtlasSprite COSMIC_5;
    public static TextureAtlasSprite COSMIC_6;
    public static TextureAtlasSprite COSMIC_7;
    public static TextureAtlasSprite COSMIC_8;
    public static TextureAtlasSprite COSMIC_9;

    public static TextureAtlasSprite INFINITY_SWORD_MASK;

    public static TextureAtlasSprite INFINITY_BOW_IDLE;
    public static TextureAtlasSprite[] INFINITY_BOW_PULL;
    public static TextureAtlasSprite INFINITY_BOW_PULL_0;
    public static TextureAtlasSprite INFINITY_BOW_PULL_1;
    public static TextureAtlasSprite INFINITY_BOW_PULL_2;

    public static TextureAtlasSprite INFINITY_BOW_IDLE_MASK;
    public static TextureAtlasSprite[] INFINITY_BOW_PULL_MASK;
    public static TextureAtlasSprite INFINITY_BOW_PULL_0_MASK;
    public static TextureAtlasSprite INFINITY_BOW_PULL_1_MASK;
    public static TextureAtlasSprite INFINITY_BOW_PULL_2_MASK;

}
