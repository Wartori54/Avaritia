package morph.avaritia.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import morph.avaritia.client.render.entity.ModelRendererWing;
import morph.avaritia.client.render.entity.OldModelArmorInfinity;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AvaritiaClientEventHandler {

    public static FloatBuffer cosmicUVs = BufferUtils.createFloatBuffer(4 * 10);

    public static IRenderTypeBuffer currBuffer;

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

    @SubscribeEvent
    public void onRenderPlayerArmorPre(RenderPlayerEvent.Pre event) {
        currBuffer = event.getBuffers();
    }

    // TextureStitchEvent gets registered on another event bus, this is to isolate it without creating another file, im lazy ok
    public static class OtherBus {
        @SubscribeEvent
        public void textureStitchPost(TextureStitchEvent.Post event) {
            if (event.getMap().location().compareTo(new ResourceLocation("minecraft:textures/atlas/blocks.png")) == 0) {
                OldModelArmorInfinity.itempagewidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                OldModelArmorInfinity.itempageheight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

//                OldModelArmorInfinity.armorModel.rebuildOverlay();
//                ModelArmorInfinity.legModel.rebuildOverlay();
//                ModelArmorInfinity.armorModel.rebuild();
            }
            String[] parts = event.getMap().location().getPath().split("/");
            String name = parts[parts.length-1];
            exportImg(event.getMap(),
                    GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH),
                    GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT),
                    name + ".png");

        }


        private static void exportImg(AtlasTexture atlas, int width, int height, String name) {
            try (NativeImage image = new NativeImage(width, height, false)) {
                int texId = atlas.getId();
                RenderSystem.bindTexture(texId);
                image.downloadTexture(0, false);
                Path folderPath = FMLPaths.GAMEDIR.get().resolve("atlases");
                if (!Files.exists(folderPath))
                    Files.createDirectory(folderPath);
                Path filePath = folderPath.resolve(name);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
                Files.createFile(filePath);

                image.writeToFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
