package morph.avaritia.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IHaloRenderItem {

    @OnlyIn(Dist.CLIENT)
    boolean shouldDrawHalo(ItemStack stack);

    @OnlyIn(Dist.CLIENT)
    TextureAtlasSprite getHaloTexture(ItemStack stack);

    @OnlyIn(Dist.CLIENT)
    int getHaloColour(ItemStack stack);

    @OnlyIn(Dist.CLIENT)
    int getHaloSize(ItemStack stack);

    @OnlyIn(Dist.CLIENT)
    boolean shouldDrawPulse(ItemStack stack);

}
