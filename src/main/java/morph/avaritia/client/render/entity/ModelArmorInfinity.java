package morph.avaritia.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import morph.avaritia.client.AvaritiaClientEventHandler;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import morph.avaritia.client.render.shader.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class ModelArmorInfinity extends BipedModel<LivingEntity> {

    public static ModelArmorInfinity armorModel = new ModelArmorInfinity(1.0f);
    public static ModelArmorInfinity legModel = new ModelArmorInfinity(0.5f);

    public static ResourceLocation eyeTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_eyes.png");
    public static ResourceLocation wingTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_wing.png");
    public static ResourceLocation wingGlowTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_wingglow.png");

    public ModelArmorInfinity(float expand) {
        super(expand, 0, 64, 32);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        super.renderToBuffer(matrixStack, vertexBuilder, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

}
