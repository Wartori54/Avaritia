package morph.avaritia.client;

import codechicken.lib.texture.TextureUtils;
//import morph.avaritia.client.render.entity.ModelArmorInfinity;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class AvaritiaClientEventHandler {

    public static FloatBuffer cosmicUVs = BufferUtils.createFloatBuffer(4 * 10);

    @SubscribeEvent
    public void textureStichPost(TextureStitchEvent.Post event) {

        TextureUtils.bindBlockTexture();
//        ModelArmorInfinity.itempagewidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
//        ModelArmorInfinity.itempageheight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
//
//        ModelArmorInfinity.armorModel.rebuildOverlay();
//        ModelArmorInfinity.legModel.rebuildOverlay();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            cosmicUVs = BufferUtils.createFloatBuffer(4 * AvaritiaTextures.COSMIC.length);
            TextureAtlasSprite icon;
            for (TextureAtlasSprite cosmicIcon : AvaritiaTextures.COSMIC) {
                icon = cosmicIcon;
                if (icon == null) {
                    break; // because all elements are loaded at once, hopefully
                }
                cosmicUVs.put(icon.getU0());
                cosmicUVs.put(icon.getV0());
                cosmicUVs.put(icon.getU1());
                cosmicUVs.put(icon.getV1());
            }
            cosmicUVs.flip();
        }
    }

    @SubscribeEvent
    public void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        CosmicShaderHelper.inventoryRender = true;
    }

    @SubscribeEvent
    public void drawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        CosmicShaderHelper.inventoryRender = false;
    }
}
