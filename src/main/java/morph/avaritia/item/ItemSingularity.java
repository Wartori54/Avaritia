package morph.avaritia.item;

import morph.avaritia.Avaritia;
import morph.avaritia.api.IHaloRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


/**
 * Created by covers1624 on 11/04/2017.
 */
public class ItemSingularity extends Item implements IHaloRenderItem, IModelRegister {

    public ItemSingularity() {
        super(new Properties().tab(Avaritia.TAB).rarity(Rarity.UNCOMMON));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDrawHalo(ItemStack stack) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getHaloTexture(ItemStack stack) {
        return AvaritiaTextures.HALO;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getHaloSize(ItemStack stack) {
        return 4;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getHaloColour(ItemStack stack) {
        return 0xFF000000;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDrawPulse(ItemStack stack) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT) // TODO: fix this
    public void registerModels() {
//        ModelResourceLocation location = new ModelResourceLocation("avaritia:singularity", "type=singularity");
//        ModelLoader.registerItemVariants(this, location);
//        IBakedModel wrappedModel = new HaloRenderItem(TransformUtils.DEFAULT_ITEM, modelRegistry -> modelRegistry.getObject(location));
//        ModelRegistryHelper.register(location, wrappedModel);
//        ModelLoader.setCustomMeshDefinition(this, stack -> location);
    }
}
