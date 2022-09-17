/*
 *
 * Code blatantly jacked from Vazkii
 * Get the original here: https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/tool/terrasteel/ItemTerraAxe.java
 */

package morph.avaritia.item.tools;

import codechicken.lib.raytracer.RayTracer;
import com.google.common.collect.Sets;
import morph.avaritia.Avaritia;
import morph.avaritia.api.InfinityItem;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.ModHelper;
import morph.avaritia.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemAxeInfinity extends AxeItem implements InfinityItem {

    public static final Set<Material> MATERIALS = Sets.newHashSet(Material.CORAL, Material.LEAVES, Material.WOOD,
            Material.VEGETABLE, Material.GRASS, Material.PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_PLANT,
            Material.REPLACEABLE_FIREPROOF_PLANT, Material.WATER_PLANT, Material.REPLACEABLE_WATER_PLANT,
            Material.NETHER_WOOD);
    public static Map<Block, Block> axeEditBlocks = new HashMap<>();

    static {
        axeEditBlocks.put(Blocks.GRASS_BLOCK, Blocks.DIRT);
        axeEditBlocks.put(Blocks.CRIMSON_NYLIUM, Blocks.NETHERRACK);
        axeEditBlocks.put(Blocks.WARPED_NYLIUM, Blocks.NETHERRACK);
    }


    public ItemAxeInfinity() {
        super(ModHelper.InfinityTier.INFINITY_TIER, 20.0F, -3.0F, new Properties().stacksTo(1).tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY).defaultDurability(9999));
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (super.getDestroySpeed(stack, state) > 1.0F || state.getMaterial() == Material.LEAVES) {
            return speed;
        }
        return Math.max(super.getDestroySpeed(stack, state), 6.0F);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            player.swing(hand);

            int range = 13;
            BlockPos min = new BlockPos(-range, -3, -range);
            BlockPos max = new BlockPos(range, range * 2 - 3, range);

            ToolHelper.aoeBlocks(player, stack, world, player.blockPosition(), min, max, null, MATERIALS, false);
            return ActionResult.success(stack);
        }
        return ActionResult.pass(stack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
        if (player.isCreative()) {
            BlockRayTraceResult traceResult = RayTracer.retrace(player, 10, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
            if (traceResult != null) {
                breakOtherBlock(player,  stack, pos, traceResult.getDirection());
            }
        }
        return false;
    }

        @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity player) {
        if (!(player instanceof PlayerEntity)) {
            return false; // hopefully this is fine
        }
        BlockRayTraceResult traceResult = RayTracer.retrace((PlayerEntity) player, 10, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
        if (traceResult != null) {
            breakOtherBlock((PlayerEntity) player, stack, pos, traceResult.getDirection());
        }
        return false;
    }

    public void breakOtherBlock(PlayerEntity player, ItemStack stack, BlockPos pos, Direction sideHit) {
        if (player.isShiftKeyDown()) {
            return;
        }
        AvaritiaEventHandler.startCrawlerTask(player.level, player, stack, pos, 32, false, true, new HashSet<>());
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
    public boolean isFoil(ItemStack par1ItemStack) {
        return false;
    }

    @Override
    public boolean isValidMaterial(Material material) {
        return MATERIALS.contains(material);
    }

    public static void setRuining(ItemStack stack, boolean value) {
        if (!(stack.getItem() instanceof ItemAxeInfinity)) return;
        stack.getOrCreateTag().putBoolean("is_ruining", value);
    }
}
