package morph.avaritia.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import morph.avaritia.Avaritia;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import morph.avaritia.item.ItemArmorInfinity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WingLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public static ResourceLocation eyeTex = new ResourceLocation(Avaritia.MOD_ID, "textures/models/infinity_armor_eyes.png");
    public static ResourceLocation wingTex = new ResourceLocation(Avaritia.MOD_ID, "textures/models/infinity_armor_wing.png");
    public static ResourceLocation wingGlowTex = new ResourceLocation(Avaritia.MOD_ID, "textures/models/infinity_armor_wingglow.png");
    public static ResourceLocation wingMaskTex = new ResourceLocation(Avaritia.MOD_ID, "textures/models/infinity_armor_mask_wings_test.png");


    private final IEntityRenderer<?, ?> renderer;

    private double animTime = 0.0D;

    public ModelRendererWing leftWing;
    public ModelRendererWing rightWing;

    public ModelRenderer leftWingOverlay;
    public ModelRenderer rightWingOverlay;

    public WingLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
        super(renderer);
        this.renderer = renderer;

        this.leftWing = new ModelRendererWing(this.getParentModel(), 0, 0);
        this.leftWing.setTexSize(64, 64);
        this.leftWing.mirror = true;
        this.leftWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
        this.leftWing.setPos(-1.5f, 0.0f, 2.0f);
        this.leftWing.yRot = (float) (Math.PI * 0.4);

        this.rightWing = new ModelRendererWing(this.getParentModel(), 0, 0);
        this.rightWing.setTexSize(64, 64);
        this.rightWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
        this.rightWing.setPos(1.5f, 0.0f, 2.0f);
        this.rightWing.yRot = (float) (-Math.PI * 0.4);
        this.rightWing.visible = true;
        this.leftWing.visible = true;

        this.leftWingOverlay = new ModelRenderer(this.getParentModel(), 0, 0);
        this.leftWingOverlay.setTexSize(64, 64);
        this.leftWingOverlay.mirror = true;
        this.leftWingOverlay.addBox(0f, -11.6f, 0f, 0, 32, 32);
        this.leftWingOverlay.setPos(-1.5f, 0.0f, 2.0f);
        this.leftWingOverlay.yRot = (float) (Math.PI * 0.4);

        this.rightWingOverlay = new ModelRendererWing(this.getParentModel(), 0, 0);
        this.rightWingOverlay.setTexSize(64, 64);
        this.rightWingOverlay.addBox(0f, -11.6f, 0f, 0, 32, 32);
        this.rightWingOverlay.setPos(1.5f, 0.0f, 2.0f);
        this.rightWingOverlay.yRot = (float) (-Math.PI * 0.4);
        this.rightWingOverlay.visible = true;
        this.leftWingOverlay.visible = true;
    }

    @Override
    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, AbstractClientPlayerEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        animTime += pPartialTicks;
        if (pLivingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ItemArmorInfinity &&
            pLivingEntity.abilities.flying) {
            CosmicShaderHelper.useShader();
//            RenderSystem.disableAlphaTest();
//            RenderSystem.enableBlend();
//            RenderSystem.depthMask(false);

            double pulse = Math.sin(pAgeInTicks / 20) * 0.5 + 0.5;
            double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;

            pMatrixStack.pushPose();
            // render wings
            IVertexBuilder wingVertexBuilder = pBuffer.getBuffer(RenderType.entityCutoutNoCull(wingTex));
            this.leftWing.render(pMatrixStack, wingVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY);
            this.rightWing.render(pMatrixStack, wingVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY);
            // render glow
            IVertexBuilder glowVertexBuilder = pBuffer.getBuffer(RenderType.entityTranslucent(wingGlowTex));
            this.leftWing.render(pMatrixStack, glowVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, (float) pulse_mag_sqr);
            this.rightWing.render(pMatrixStack, glowVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, (float) pulse_mag_sqr);

            // render overlay
            IVertexBuilder overlayVertexBuilder = pBuffer.getBuffer(StaticAccessor.WING_ANIM);
            this.leftWingOverlay.render(pMatrixStack, overlayVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            this.rightWingOverlay.render(pMatrixStack, overlayVertexBuilder, pPackedLight, OverlayTexture.NO_OVERLAY);
            pMatrixStack.popPose();
//            RenderSystem.depthMask(true);
//            RenderSystem.disableBlend();
//            RenderSystem.enableAlphaTest();
            CosmicShaderHelper.releaseShader();
        }
    }

    @SuppressWarnings("deprecation")
    static class StaticAccessor extends RenderState {
        public static final RenderState.TexturingState GLOW_TEXTURING =
                new RenderState.TexturingState("glow_texturing", () -> {
                    RenderSystem.matrixMode(5890);
                    RenderSystem.pushMatrix();
                    RenderSystem.loadIdentity();
                    long i = Util.getMillis() / 600;
                    float vPos = (i % 10);
                    RenderSystem.scalef(1F,1F/10F, 1F);
                    RenderSystem.translatef(0.0F, vPos, 0.0F);
                    RenderSystem.matrixMode(5888);
                }, () -> {
                    RenderSystem.matrixMode(5890);
                    RenderSystem.popMatrix();
                    RenderSystem.matrixMode(5888);
                });

        public static final RenderType WING_ANIM = RenderType.create(
                "wing_anim",
                DefaultVertexFormats.NEW_ENTITY,
                7,
                256,
                true,
                true,
                RenderType.State.builder()
                        .setTextureState(
                                new RenderState.TextureState(
                                        wingMaskTex,
                                        false,
                                        false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setDiffuseLightingState(DIFFUSE_LIGHTING)
                        .setAlphaState(DEFAULT_ALPHA)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setTexturingState(GLOW_TEXTURING)
                        .createCompositeState(true));

        public StaticAccessor(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
            super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
            throw new IllegalStateException("This class must not be instantiated");
        }
    }

}
