package morph.avaritia.item.tools;

import codechicken.lib.model.bakery.IBakeryProvider;
import codechicken.lib.model.bakery.generation.IBakery;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.InfinityBowModelWrapper;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemBowInfinity extends Item implements ICosmicRenderItem, IModelRegister, IBakeryProvider {

    //private IIcon[] iconArray;
    //private IIcon[] maskArray;
    //private IIcon idleMask;

    public ItemBowInfinity() {
        super(new Properties().stacksTo(1).tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY).defaultDurability(9999));
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public void onUseTick(World world, LivingEntity player, ItemStack stack, int count) {
        if (count == 1) {
            fire(stack, world, player, 0);
        }
    }

    public void fire(ItemStack stack, World world, LivingEntity player, int useCount) {
        int max = getUseDuration(stack);
        float maxf = (float) max;
        int j = max - useCount;

        float f = j / maxf;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f < 0.1) {
            return;
        }

        if (f > 1.0) {
            f = 1.0F;
        }

        EntityHeavenArrow arrow = new EntityHeavenArrow(player, world);
        Vector2f rotVec = player.getRotationVector();
        arrow.shootFromRotation(player, rotVec.x, rotVec.y, 0, f * 3.0F, 1.0F);//TODO, no inaccuracy?
        arrow.setBaseDamage(20.0);

        if (f == 1.0F) {
            arrow.setCritArrow(true);
        }

        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);

        if (k > 0) {
            arrow.setBaseDamage(arrow.getBaseDamage() + k + 1);
        }

        int l = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);

        if (l > 0) {
            arrow.setKnockback(l);
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
            arrow.setSecondsOnFire(100);
        }

        stack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(playerEntity.getUsedItemHand()));
        world.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
        arrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;

        if (!world.isClientSide()) {
            world.addFreshEntity(arrow);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 13;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getItemInHand(hand);
        ActionResult<ItemStack> event = ForgeEventFactory.onArrowNock(stack, world, player, hand, true);
        if (event != null) {
            return event;
        }

        player.startUsingItem(hand);

        return ActionResult.consume(stack);
//        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    //@Override
    //@OnlyIn(Dist.CLIENT)
    //public void registerIcons(IIconRegister ir) {
    //    int pullframes = 3;
    //    this.itemIcon = ir.registerIcon(this.getIconString() + "_standby");
    //    this.idleMask = ir.registerIcon(this.getIconString() + "_standby_mask");
    //    this.iconArray = new IIcon[pullframes];
    //    this.maskArray = new IIcon[pullframes];
    //    for (int i = 0; i < pullframes; ++i) {
    //        this.iconArray[i] = ir.registerIcon(this.getIconString() + "_pulling_" + i);
    //        this.maskArray[i] = ir.registerIcon(this.getIconString() + "_pulling_mask_" + i);
    //    }
    //}

    //@Override
    //@OnlyIn(Dist.CLIENT)
    //public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
    //    if (usingItem != null) {
    //        int max = stack.getMaxItemUseDuration();
    //        int pull = max - useRemaining;
    //        if (pull >= (max * 2) / 3.0) {
    //            return this.iconArray[2];
    //        }
    //        if (pull > max / 3.0) {
    //            return this.iconArray[1];
    //        }
    //        if (pull > 0) {
    //            return this.iconArray[0];
    //        }
    //    }
    //    return getIcon(stack, renderPass);
    //}

    //@Override
    //@OnlyIn(Dist.CLIENT)
    //public IIcon getIcon(ItemStack stack, int pass) {
    //    return super.getIcon(stack, pass);
    //}

//    @Override
//    @OnlyIn(Dist.CLIENT) // TODO: wtf is this
//    public boolean isFull3D() {
//        return true;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getMaskTexture(ItemStack stack, LivingEntity player) {
        int frame = -1;
        if (player != null) {
            int bframe = InfinityBowModelWrapper.getBowFrame(player);
            frame = bframe != 0 ? bframe : -1;
        }
        //Lumberjack.info(frame);
        if (frame == -1) {
            return AvaritiaTextures.INFINITY_BOW_IDLE_MASK;
        }
        return AvaritiaTextures.INFINITY_BOW_PULL_MASK[frame];
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getMaskOpacity(ItemStack stack, LivingEntity player) {
        return 1.0f;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerModels() {
//        ModelBakery.registerItemKeyGenerator(this, stack -> {
//            String key = ModelBakery.defaultItemKeyGenerator.generateKey(stack);
//            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("frame")) {
//                key += "@pull=" + stack.getTagCompound().getInteger("frame");
//            }
//            return key;
//        });
//        ModelResourceLocation location = new ModelResourceLocation("avaritia:bow", "bow");
//        IBakedModel actualModel = new InfinityBowModelWrapper();
//        IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_BOW, modelRegistry -> actualModel);
//        ModelRegistryHelper.register(location, wrapped);
//        ModelLoader.setCustomMeshDefinition(this, stack -> location);
//
    }

    @Override
    public IBakery getBakery() {
//        return InfinityBowModelBakery.INSTANCE;
        return null;
    }
}
