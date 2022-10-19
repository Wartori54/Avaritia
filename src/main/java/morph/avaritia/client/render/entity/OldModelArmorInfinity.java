package morph.avaritia.client.render.entity;

import codechicken.lib.math.MathHelper;
import codechicken.lib.texture.TextureUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import morph.avaritia.client.AvaritiaClientEventHandler;
import morph.avaritia.client.ColourHelper;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@SuppressWarnings({"deprecation", "unused"})
@OnlyIn(Dist.CLIENT)
public class OldModelArmorInfinity extends BipedModel<LivingEntity> {

    public static final OldModelArmorInfinity armorModel = new OldModelArmorInfinity(1.0f);
//    public static final ModelArmorInfinity legModel = new ModelArmorInfinity(0.5f);

    public static ResourceLocation eyeTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_eyes.png");
    public static ResourceLocation wingTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_wing.png");
    public static ResourceLocation wingGlowTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_wingglow.png");
    public static int itempagewidth = 0;
    public static int itempageheight = 0;
    public boolean legs = false;

    public EquipmentSlotType currentSlot = EquipmentSlotType.HEAD;

    private final Random randy = new Random();

    private final Overlay overlay;
    private final Overlay invulnOverlay;
    private boolean invulnRender = false;
    private boolean showHat;
    private boolean showChest;
    private boolean showLeg;
    private boolean showFoot;

    private final float expand;

    public ModelRenderer bipedLeftWing;
    public ModelRenderer bipedRightWing;

    public OldModelArmorInfinity(float expand) {
        super(expand, 0, 64, 32);
        this.expand = expand;
        overlay = new Overlay(this, expand);
        invulnOverlay = new Overlay(this, 0);

//        hat = new ModelRenderer(this, 32, 0);
//        hat.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expand * 0.5F);
//        hat.setPos(0.0F, 0.0F + 0, 0.0F);
    }

    public OldModelArmorInfinity setLegs(boolean islegs) {
        legs = islegs;

        int heightoffset = 0;
        int legoffset = islegs ? 32 : 0;

        body = new ModelRenderer(this, 16, 16 + legoffset);
        body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, expand);
        body.setPos(0.0F, 0.0F + heightoffset, 0.0F);
        rightLeg = new ModelRenderer(this, 0, 16 + legoffset);
        rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, expand);
        rightLeg.setPos(-1.9F, 12.0F + heightoffset, 0.0F);
        leftLeg = new ModelRenderer(this, 0, 16 + legoffset);
        leftLeg.mirror = true;
        leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, expand);
        leftLeg.setPos(1.9F, 12.0F + heightoffset, 0.0F);

        return this;
    }

    public void rebuildWings() {

        // remove the old items from the list so that the new ones don't just stack up
        if (bipedLeftWing != null) {
            body.children.remove(this.overlay.bipedLeftWing);
        }
        if (bipedRightWing != null) {
            body.children.remove(this.overlay.bipedRightWing);
        }

        // define new
        bipedLeftWing = new ModelRendererWing(this.texWidth, 64, 0, 0);
        bipedLeftWing.mirror = true;
        bipedLeftWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
        bipedLeftWing.setPos(-1.5f, 0.0f, 2.0f);
        bipedLeftWing.yRot = (float) (Math.PI * 0.4);
        body.addChild(bipedLeftWing);

        bipedRightWing = new ModelRendererWing(this.texWidth, 64, 0, 0);
        bipedRightWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
        bipedRightWing.setPos(1.5f, 0.0f, 2.0f);
        bipedRightWing.yRot = (float) (-Math.PI * 0.4);
        body.addChild(bipedRightWing);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder vertexBuilder, int f, int f1, float f2, float f3, float f4, float f5) {
        Minecraft mc = Minecraft.getInstance();
        //copyBipedAngles(this, this.overlay);
        //copyBipedAngles(this, this.invulnOverlay);

        super.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);
        if (mc.player == null) return;
        boolean isFlying = mc.player.abilities.flying && mc.player.hasImpulse;

        RenderSystem.color4f(1, 1, 1, 1);
        CosmicShaderHelper.useShader();
        TextureUtils.bindBlockTexture();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.depthMask(false);
        if (invulnRender) {
            RenderSystem.color4f(1, 1, 1, 0.2F);
            invulnOverlay.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);
        }
//        overlay.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);

        CosmicShaderHelper.releaseShader();

        mc.textureManager.bind(eyeTex);
        RenderSystem.disableLighting();
        mc.getEntityRenderDispatcher().setRenderShadow(false);

        long time = mc.player.level.getDayTime();

        setGems();

        double pulse = Math.sin(time / 10.0) * 0.5 + 0.5;
        double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;
        RenderSystem.color4f(0.84F, 1F, 0.95F, (float) (pulse_mag_sqr * 0.5F));

        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        super.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();

        if (invulnRender) {
            long frame = time / 3;
            randy.setSeed(frame * 1723609L);
            float o = randy.nextFloat() * 6.0f;
            float[] col = ColourHelper.HSVtoRGB(o, 1.0f, 1.0f);

            RenderSystem.color4f(col[0], col[1], col[2], 1);
            setEyes();
            super.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);
        }

        if (!CosmicShaderHelper.inventoryRender) {
            mc.getEntityRenderDispatcher().setRenderShadow(true);
        }
        RenderSystem.enableLighting();
        RenderSystem.color4f(1, 1, 1, 1);

        // WINGS
        if (isFlying && !CosmicShaderHelper.inventoryRender) {
            setWings();
//            mc.getTextureManager().bind(wingTex);
//            BufferBuilder wingVertexBuilder = (BufferBuilder) AvaritiaClientEventHandler.currBuffer.getBuffer(RenderType.entityCutoutNoCull(wingTex));
            super.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);

            CosmicShaderHelper.useShader();
            TextureUtils.bindBlockTexture();
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.depthMask(false);
//            overlay.renderToBuffer(stack, wingVertexBuilder, f, f1, f2, f3, f4, f5);

            CosmicShaderHelper.releaseShader();

            mc.getTextureManager().bind(wingGlowTex);
            RenderSystem.disableLighting();
            mc.getEntityRenderDispatcher().setRenderShadow(false);


            RenderSystem.color4f(0.84F, 1F, 0.95F, (float) (pulse_mag_sqr * 0.5));

            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            super.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            if (!CosmicShaderHelper.inventoryRender) {
                mc.getEntityRenderDispatcher().setRenderShadow(true);

            }
            RenderSystem.enableLighting();
            RenderSystem.color4f(1, 1, 1, 1);

        }

    }

    public void update(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlotType armorSlot) {
        currentSlot = armorSlot;

        invulnRender = false;

        ItemStack hat = entityLiving.getItemBySlot(EquipmentSlotType.HEAD);
        ItemStack chest = entityLiving.getItemBySlot(EquipmentSlotType.CHEST);
        ItemStack leg = entityLiving.getItemBySlot(EquipmentSlotType.LEGS);
        ItemStack foot = entityLiving.getItemBySlot(EquipmentSlotType.FEET);

        boolean hasHat = hat != ItemStack.EMPTY && hat.getItem() == ModItems.infinity_helmet; //&& !((ItemArmorInfinity) (ModItems.infinity_helmet)).hasPhantomInk(hat);
        boolean hasChest = chest != ItemStack.EMPTY && chest.getItem() == ModItems.infinity_chestplate; // && !((ItemArmorInfinity) (ModItems.infinity_chestplate)).hasPhantomInk(chest);
        boolean hasLeg = leg != ItemStack.EMPTY && leg.getItem() == ModItems.infinity_pants; // && !((ItemArmorInfinity) (ModItems.infinity_pants)).hasPhantomInk(leg);
        boolean hasFoot = foot != ItemStack.EMPTY && foot.getItem() == ModItems.infinity_boots; // && !((ItemArmorInfinity) (ModItems.infinity_boots)).hasPhantomInk(foot);

        if (armorSlot == EquipmentSlotType.HEAD) {//TODO, Wot.
            if (hasHat && hasChest && hasLeg && hasFoot) {
                invulnRender = true;
            }
        }

        showHat = hasHat && armorSlot == EquipmentSlotType.HEAD;
        showChest = hasChest && armorSlot == EquipmentSlotType.CHEST;
        showLeg = hasLeg && armorSlot == EquipmentSlotType.LEGS;
        showFoot = hasFoot && armorSlot == EquipmentSlotType.FEET;

        head.visible = showHat;
        this.hat.visible = showHat;
        body.visible = showChest || showLeg;
        rightArm.visible = showChest;
        leftArm.visible = showChest;
        rightLeg.visible = showLeg || showFoot;
        leftLeg.visible = showLeg || showFoot;

        overlay.head.visible = showHat;
        overlay.hat.visible = showHat;
        overlay.body.visible = showChest || showLeg;
        overlay.rightArm.visible = showChest;
        overlay.leftArm.visible = showChest;
        overlay.rightLeg.visible = showLeg || showFoot;
        overlay.leftLeg.visible = showLeg || showFoot;

        bipedLeftWing.visible = false;
        bipedRightWing.visible = false;
        overlay.bipedLeftWing.visible = false;
        overlay.bipedRightWing.visible = false;

        crouching = entityLiving.isShiftKeyDown();
        riding = entityLiving.isPassenger();
        young = entityLiving.isBaby();

        overlay.crouching = entityLiving.isShiftKeyDown();
        overlay.riding = entityLiving.isPassenger();
        overlay.young = entityLiving.isBaby();

        invulnOverlay.crouching = entityLiving.isShiftKeyDown();
        invulnOverlay.riding = entityLiving.isPassenger();
        invulnOverlay.young = entityLiving.isBaby();

        overlay.swimAmount = swimAmount;
        invulnOverlay.swimAmount = swimAmount;

        leftArmPose = ArmPose.EMPTY;
        rightArmPose = ArmPose.EMPTY;

        overlay.leftArmPose = ArmPose.EMPTY;
        overlay.rightArmPose = ArmPose.EMPTY;

        invulnOverlay.leftArmPose = ArmPose.EMPTY;
        invulnOverlay.rightArmPose = ArmPose.EMPTY;

        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;

            ItemStack main_hand = player.getMainHandItem();

            if (main_hand != ItemStack.EMPTY) {
                rightArmPose = ArmPose.ITEM;
                overlay.rightArmPose = ArmPose.ITEM;
                invulnOverlay.rightArmPose = ArmPose.ITEM;

                if (player.getUseItemRemainingTicks() > 0) {

                    UseAction enumaction = main_hand.getUseAnimation();

                    if (enumaction == UseAction.BOW) {
                        rightArmPose = ArmPose.BOW_AND_ARROW;
                        overlay.rightArmPose = ArmPose.BOW_AND_ARROW;
                        invulnOverlay.rightArmPose = ArmPose.BOW_AND_ARROW;
                    } else if (enumaction == UseAction.BLOCK) {
                        rightArmPose = ArmPose.BLOCK;
                        overlay.rightArmPose = ArmPose.BLOCK;
                        invulnOverlay.rightArmPose = ArmPose.BLOCK;
                    }

                }

            }

            ItemStack off_hand = player.getOffhandItem();
            if (off_hand != ItemStack.EMPTY) {
                leftArmPose = ArmPose.ITEM;
                overlay.leftArmPose = ArmPose.ITEM;
                invulnOverlay.leftArmPose = ArmPose.ITEM;

                if (player.getUseItemRemainingTicks() > 0) {

                    UseAction enumaction = off_hand.getUseAnimation();

                    if (enumaction == UseAction.BOW) {
                        leftArmPose = ArmPose.BOW_AND_ARROW;
                        overlay.leftArmPose = ArmPose.BOW_AND_ARROW;
                        invulnOverlay.leftArmPose = ArmPose.BOW_AND_ARROW;
                    } else if (enumaction == UseAction.BLOCK) {
                        leftArmPose = ArmPose.BLOCK;
                        overlay.leftArmPose = ArmPose.BLOCK;
                        invulnOverlay.leftArmPose = ArmPose.BLOCK;
                    }

                }

            }
        }

    }

    @ParametersAreNonnullByDefault
    @Override
    public void setupAnim(LivingEntity entity, float f1, float speed, float ticks, float headYaw, float headPitch) {
        super.setupAnim(entity, f1, speed, ticks, headYaw, headPitch);
        overlay.setupAnim(entity, f1, speed, ticks, headYaw, headPitch);
        invulnOverlay.setupAnim(entity, f1, speed, ticks, headYaw, headPitch);
        EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
        if (manager.renderers.containsKey(entity.getType())) {
            EntityRenderer<?> r = manager.renderers.get(entity.getType());

            if (r instanceof BipedRenderer) {
                BipedModel<?> m = ((BipedRenderer<?, ?>) r).getModel();

                copyBipedAngles(m, this);
            }
        }
    }

    public void setEyes() {
        head.visible = false;
        body.visible = false;
        rightArm.visible = false;
        leftArm.visible = false;
        rightLeg.visible = false;
        leftLeg.visible = false;
        hat.visible = showHat;
    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    public void setGems() {
        head.visible = false;
        hat.visible = false;
        body.visible = legs ? false : showChest;
        rightArm.visible = legs ? false : showChest;
        leftArm.visible = legs ? false : showChest;
        rightLeg.visible = legs ? showLeg : false;
        leftLeg.visible = legs ? showLeg : false;
    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    public void setWings() {
        body.visible = legs ? false : showChest;
        bipedLeftWing.visible = true;
        bipedRightWing.visible = true;
        rightArm.visible = false;
        leftArm.visible = false;
        rightLeg.visible = false;
        leftLeg.visible = false;
        hat.visible = false;
        head.visible = false;

        overlay.body.visible = legs ? false : showChest;
        overlay.bipedLeftWing.visible = true;
        overlay.bipedRightWing.visible = true;
        overlay.head.visible = false;
        overlay.hat.visible = false;
    }

    public void rebuildOverlay() {
        rebuildWings();
        overlay.rebuild(AvaritiaTextures.INFINITY_ARMOR_MASK, AvaritiaTextures.INFINITY_ARMOR_MASK_WINGS);
        invulnOverlay.rebuild(AvaritiaTextures.INFINITY_ARMOR_MASK_INV, null);
    }

    public static void copyPartAngles(ModelRenderer source, ModelRenderer dest) {
        dest.xRot = source.xRot;
        dest.yRot = source.yRot;
        dest.zRot = source.zRot;
    }

    public static void copyBipedAngles(BipedModel<?> source, BipedModel<?> dest) {
        copyPartAngles(source.body, dest.body);
        copyPartAngles(source.head, dest.head);
        copyPartAngles(source.hat, dest.hat);
        copyPartAngles(source.leftArm, dest.leftArm);
        copyPartAngles(source.leftLeg, dest.leftLeg);
        copyPartAngles(source.rightArm, dest.rightArm);
        copyPartAngles(source.rightLeg, dest.rightLeg);
    }

    public static class Overlay extends BipedModel<LivingEntity> {

        public OldModelArmorInfinity parent;
        public float expand;

        public ModelRenderer bipedLeftWing;
        public ModelRenderer bipedRightWing;

        public Overlay(OldModelArmorInfinity parent, float expand) {
            super(expand, 0, 64, 32);
            this.parent = parent;
            this.expand = expand;
        }



        public void rebuild(TextureAtlasSprite icon, @Nullable TextureAtlasSprite wingicon) {
            int ox = MathHelper.floor(icon.getU0() * itempagewidth);
            int oy = MathHelper.floor(icon.getV0() * itempageheight);

            float heightoffset = 0.0f;
            int legoffset = parent.legs ? 32 : 0;

            texWidth = itempagewidth;
            texHeight = itempageheight;
//            head = new ModelRenderer(this, 0 + ox, 0 + legoffset + oy);
//            head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expand);
//            head.setPos(0.0F, 0.0F + heightoffset, 0.0F);
//            hat = new ModelRenderer(this, 32 + ox, 0 + legoffset + oy);
//            hat.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expand * 0.5F);
//            hat.setPos(0.0F, 0.0F + heightoffset, 0.0F);
//            body = new ModelRenderer(this, 16 + ox, 16 + legoffset + oy);
//            body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, expand);
//            body.setPos(0.0F, 0.0F + heightoffset, 0.0F);
//            rightArm = new ModelRenderer(this, 40 + ox, 16 + legoffset + oy);
//            rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, expand);
//            rightArm.setPos(-5.0F, 2.0F + heightoffset, 0.0F);
//            leftArm = new ModelRenderer(this, 40 + ox, 16 + legoffset + oy);
//            leftArm.mirror = true;
//            leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, expand);
//            leftArm.setPos(5.0F, 2.0F + heightoffset, 0.0F);
//            rightLeg = new ModelRenderer(this, 0 + ox, 16 + legoffset + oy);
//            rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, expand);
//            rightLeg.setPos(-1.9F, 12.0F + heightoffset, 0.0F);
//            leftLeg = new ModelRenderer(this, 0 + ox, 16 + legoffset + oy);
//            leftLeg.mirror = true;
//            leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, expand);
//            leftLeg.setPos(1.9F, 12.0F + heightoffset, 0.0F);
            // rebuild wings!
            if (wingicon != null) {
//                int oxw = MathHelper.floor(wingicon.getU0() * itempagewidth);
//                int oyw = MathHelper.floor(wingicon.getV0() * itempageheight);

                if (bipedLeftWing != null) {
                    body.children.remove(bipedLeftWing);
                }
                if (bipedRightWing != null) {
                    body.children.remove(bipedRightWing);
                }

                bipedLeftWing = new ModelRendererWing(this.texWidth, 64, 0, 0);
                bipedLeftWing.mirror = true;
                bipedLeftWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
                bipedLeftWing.setPos(-1.5f, 0.0f, 2.0f);
                bipedLeftWing.yRot = (float) (Math.PI * 0.4);
                body.addChild(bipedLeftWing);

                bipedRightWing = new ModelRendererWing(this.texWidth, 64, 0, 0);
                bipedRightWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
                bipedRightWing.setPos(1.5f, 0.0f, 2.0f);
                bipedRightWing.yRot = (float) (-Math.PI * 0.4);
                body.addChild(bipedRightWing);
            }
        }

        @ParametersAreNonnullByDefault
        @Override
        public void renderToBuffer(MatrixStack stack, IVertexBuilder vertexBuilder, int f, int f1, float f2, float f3, float f4, float f5) {
            copyBipedAngles(parent, this);
            super.renderToBuffer(stack, vertexBuilder, f, f1, f2, f3, f4, f5);
        }

        @ParametersAreNonnullByDefault
        @Override
        public void setupAnim(LivingEntity entity, float f1, float f2, float f3, float f4, float f5) {
            super.setupAnim(entity, f1, f2, f3, f4, f5);
            EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
            if (manager.renderers.containsKey(entity.getType())) {
                EntityRenderer<?> r = manager.renderers.get(entity.getType());

                if (r instanceof BipedRenderer) {
                    BipedModel<?> m = ((BipedRenderer<?, ?>) r).getModel();

                    copyBipedAngles(m, this);
                }
            }
        }
    }
}
