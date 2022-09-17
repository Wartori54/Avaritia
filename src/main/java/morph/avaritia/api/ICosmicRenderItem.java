package morph.avaritia.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Any item implementing this that also binds itself to TODO INPUT MODEL NAME HERE.
 * Will automatically have the cosmic shader applied to the mask with the given opacity.
 */
public interface ICosmicRenderItem {

    /**
     * The mask where the cosmic overlay will be.
     *
     * @param stack  The stack being rendered.
     * @param player The entity holding the item, May be null, If null assume either inventory, or ground.
     * @return The masked area where the cosmic overlay will be.
     */
    @OnlyIn(Dist.CLIENT)
    TextureAtlasSprite getMaskTexture(ItemStack stack, @Nullable LivingEntity player);

    /**
     * The opacity that the mask overlay will be rendered with.
     *
     * @param stack  The stack being rendered.
     * @param player The entity holding the item, May be null, If null assume either inventory, or ground.
     * @return The opacity that the mask overlay will be rendered with.
     */
    @OnlyIn(Dist.CLIENT)
    float getMaskOpacity(ItemStack stack, @Nullable LivingEntity player);
}
