package morph.avaritia.item.tools;

import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.DamageSourceInfinitySword;
import morph.avaritia.util.ModHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSwordInfinity extends SwordItem implements ICosmicRenderItem, IModelRegister {

//    private static final TOOL_MATERIAL = EnumHelper.addToolMaterial("INFINITY_SWORD", 32, 9999, 9999F, -3.0F, 200);
    //private IIcon cosmicMask;
    //private IIcon pommel;

    public ItemSwordInfinity() {
        super(ModHelper.InfinityTier.INFINITY_TIER, 0, -2.4F, new Properties().stacksTo(1).tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY).defaultDurability(9999));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) { // TODO: check if correct
        if (attacker.level.isClientSide()) {
            return true;
        }
        if (victim instanceof PlayerEntity) {
            PlayerEntity pvp = (PlayerEntity) victim;
            if (AvaritiaEventHandler.isInfinite(pvp)) {
                victim.hurt(new DamageSourceInfinitySword(attacker).bypassArmor(), 4.0F);
                return true;
            }
            if (pvp.isHolding(ModItems.infinity_sword) && pvp.isUsingItem()) {
                return true;
            }
        }
        if (attacker instanceof PlayerEntity) {
            victim.setLastHurtByPlayer((PlayerEntity) attacker);
        } else {
            victim.setLastHurtByMob(attacker);
        }
        victim.getCombatTracker().recordDamage(new DamageSourceInfinitySword(attacker), victim.getHealth(), victim.getHealth());
        victim.setHealth(0);
        victim.die(new EntityDamageSource("infinity", attacker));
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) { // kill creative players
        if (entity.level.isClientSide() || !(entity instanceof PlayerEntity)) {
            return false;
        }
        PlayerEntity victim = (PlayerEntity) entity;
        if (victim.isCreative() && !victim.isDeadOrDying() && victim.getHealth() > 0 && !AvaritiaEventHandler.isInfinite(victim)) {
            victim.getCombatTracker().recordDamage(new DamageSourceInfinitySword(player), victim.getHealth(), victim.getHealth());
            victim.setHealth(0);
            victim.die(new EntityDamageSource("infinity", player));
            //TODO
            //player.addStat(Achievements.creative_kill, 1);
            return true;
        }

        return false;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getMaskTexture(ItemStack stack, LivingEntity player) {
        return AvaritiaTextures.INFINITY_SWORD_MASK;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getMaskOpacity(ItemStack stack, LivingEntity player) {
        return 1.0f;
    }

    //@OnlyIn(Dist.CLIENT)
    //@Override
    //public void registerIcons(IIconRegister ir) {
    //    super.registerIcons(ir);
    //    this.cosmicMask = ir.registerIcon("avaritia:infinity_sword_mask");
    //    this.pommel = ir.registerIcon("avaritia:infinity_sword_pommel");
    //}

    //@Override
    //public IIcon getIcon(ItemStack stack, int pass) {
    //    if (pass == 1) {
    //        return this.pommel;
    //    }
    //    return super.getIcon(stack, pass);
    // }

    //@OnlyIn(Dist.CLIENT)
    //@Override
    //public boolean requiresMultipleRenderPasses() {
    //    return true;
    //}

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack par1ItemStack) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerModels() { // TODO: fix this
//        ModelResourceLocation sword = new ModelResourceLocation("avaritia:tools", "type=infinity_sword");
//        ModelLoader.registerItemVariants(ModItems.infinity_pickaxe, sword);
//        IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, modelRegistry -> modelRegistry.getObject(sword));
//        ModelRegistryHelper.register(sword, wrapped);
//        ModelLoader.setCustomMeshDefinition(ModItems.infinity_sword, (ItemStack stack) -> sword);
    }


}
