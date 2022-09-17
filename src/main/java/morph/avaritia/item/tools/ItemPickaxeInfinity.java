package morph.avaritia.item.tools;

import codechicken.lib.math.MathHelper;
import codechicken.lib.raytracer.RayTracer;
import com.google.common.collect.Sets;
import morph.avaritia.Avaritia;
import morph.avaritia.api.InfinityItem;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.Lumberjack;
import morph.avaritia.util.ModHelper;
import morph.avaritia.util.ToolHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.Set;

public class ItemPickaxeInfinity extends PickaxeItem implements InfinityItem {

//    private static final ToolMaterial TOOL_MATERIAL = EnumHelper.addToolMaterial("INFINITY_PICKAXE", 32, 9999, 9999F, 6.0F, 200);
    //private IIcon hammer;

    public static final Set<Material> MATERIALS = Sets.newHashSet(Material.STONE, Material.METAL, Material.ICE, Material.GLASS, Material.PISTON, Material.HEAVY_METAL, Material.GRASS, Material.DIRT, Material.SAND, Material.SNOW, Material.TOP_SNOW, Material.CLAY);
    private static final int FORTUNE_LEVEL = 40;

    public ItemPickaxeInfinity() {
        super(ModHelper.InfinityTier.INFINITY_TIER, 1, -2.8F, new Properties().stacksTo(1).tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY).defaultDurability(9999));
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

//    @OnlyIn(Dist.CLIENT) // TODO: fix hammah
//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list) {
//        if (allowdedIn(tab)) {
//            ItemStack pick = new ItemStack(this);
//            pick.addEnchantment(Enchantments.FORTUNE, 10);
//            list.add(pick);
//        }
//    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getTag() != null && stack.getTag().getBoolean("hammer")) {
            return 5.0F;
        }
        for (ToolType type : getToolTypes(stack)) {
            if (state.getBlock().isToolEffective(state, type)) {
                return speed;
            }
        }
        return Math.max(super.getDestroySpeed(stack, state), 6.0F);
    }

    @Override
    public void onCraftedBy(ItemStack stack, World world, PlayerEntity player) {
        updateEnchants(stack);
    }

    //@OnlyIn(Dist.CLIENT)
    //public void registerIcons(IIconRegister ir) {
    //    this.itemIcon = ir.registerIcon("avaritia:infinity_pickaxe");
    //    hammer = ir.registerIcon("avaritia:infinity_hammer");
    //}

    //@Override
    //public IIcon getIcon(ItemStack stack, int pass) {
    //    NBTTagCompound tags = stack.getTagCompound();
    //    if (tags != null) {
    //        if (tags.getBoolean("hammer")) {
    //            return hammer;
    //        }
    //    }
    //    return itemIcon;
    //}

    //@OnlyIn(Dist.CLIENT)
    //@Override
    //public IIcon getIconIndex(ItemStack stack) {
    //    return getIcon(stack, 0);
    //}

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            CompoundNBT tags = stack.getTag();
            if (tags == null) {
                tags = new CompoundNBT();
                stack.setTag(tags);
            }
            updateEnchants(stack);
            tags.putBoolean("hammer", !tags.getBoolean("hammer"));
            player.swing(hand);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    private static void updateEnchants(ItemStack stack) {
        int lvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack);
        if (lvl == 0) {
            stack.enchant(Enchantments.BLOCK_FORTUNE, FORTUNE_LEVEL);
        } else if (lvl < FORTUNE_LEVEL) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
            enchants.replace(Enchantments.BLOCK_FORTUNE, FORTUNE_LEVEL);
            EnchantmentHelper.setEnchantments(enchants, stack);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity player) {
        if (stack.getTag() != null) {
            if (stack.getTag().getBoolean("hammer")) {
                if (!(victim instanceof PlayerEntity && AvaritiaEventHandler.isInfinite((PlayerEntity) victim))) {
                    int i = 10;
                    Vector3d movement = victim.getDeltaMovement();
                    victim.setDeltaMovement(movement.add(-Math.sin(player.yRot * (float) Math.PI / 180.0F) * i * 0.5F, 2.0D, Math.cos(player.xRot * (float) Math.PI / 180.0F) * i * 0.5F));
                }
            }
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        if (player.isCreative() && itemstack.getOrCreateTag().getBoolean("hammer")) {
            BlockRayTraceResult traceResult = RayTracer.retrace((PlayerEntity) player,
                    10, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
            if (traceResult != null) {
                breakOtherBlock(player, itemstack, pos, traceResult.getDirection());
            }
        }
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity player) {
        if (stack.getTag() != null && stack.getTag().getBoolean("hammer") && player instanceof PlayerEntity) {
            BlockRayTraceResult traceResult = RayTracer.retrace((PlayerEntity) player,
                    10, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
            if (traceResult != null) {
                breakOtherBlock((PlayerEntity) player, stack, pos, traceResult.getDirection());
            }
        }
        return false;
    }

    public void breakOtherBlock(PlayerEntity player, ItemStack stack, BlockPos pos, Direction sideHit) {

        World world = player.level;
        BlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        if (!isValidMaterial(mat)) {
            return;
        }

        if (state.getBlock().isAir(state, world, pos)) {
            return;
        }

        boolean doY = sideHit.getAxis() != Direction.Axis.Y;

        int range = 8;
        BlockPos minOffset = new BlockPos(-range, doY ? -1 : -range, -range);
        BlockPos maxOffset = new BlockPos(range, doY ? range * 2 - 2 : range, range);

        ToolHelper.aoeBlocks(player, stack, world, pos, minOffset, maxOffset, null, MATERIALS, true);

    }

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
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidMaterial(Material material) {
        return MATERIALS.contains(material);
    }
}
