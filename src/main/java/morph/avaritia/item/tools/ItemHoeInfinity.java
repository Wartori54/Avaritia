package morph.avaritia.item.tools;

import codechicken.lib.util.ItemUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.ItemMatterCluster;
import morph.avaritia.util.ModHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;
import java.util.Set;

/**
 * Created by covers1624 on 31/07/2017.
 * Credits mostly to brandon3055, this is his AOE code.
 */
public class ItemHoeInfinity extends HoeItem {

    public ItemHoeInfinity() {
        super(ModHelper.InfinityTier.INFINITY_TIER, -4, 0F, new Properties().stacksTo(1).tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY).defaultDurability(9999));
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        Hand hand = ctx.getHand();
        World world = ctx.getLevel();
        Direction facing = ctx.getClickedFace();
        BlockPos origin = ctx.getClickedPos();
        ItemStack stack = player.getItemInHand(hand);
        if (!attemptHoe(ctx)) {
//            if (world.getBlockState(origin).getBlock() != Blocks.FARMLAND) { // TODO: check if this if is necessary
                return ActionResultType.FAIL;
//            }
        }

        if (player.isShiftKeyDown()) {
            return ActionResultType.SUCCESS;
        }

        int aoe_range = 4;
//        AvaritiaEventHandler.enableItemCapture();
        for (BlockPos aoePos : BlockPos.betweenClosed(origin.offset(-aoe_range, 0, -aoe_range), origin.offset(aoe_range, 0, aoe_range))) {
            if (aoePos.equals(origin)) {
                continue;
            }

            boolean airOrReplaceable = world.isEmptyBlock(aoePos) || world.getBlockState(aoePos).getMaterial().isReplaceable();
            boolean lowerBlockOk = world.getBlockState(aoePos.below()).isFaceSturdy(world, aoePos, Direction.UP) || world.getBlockState(aoePos.below()).getBlock() == Blocks.FARMLAND;

            if (airOrReplaceable && lowerBlockOk && (player.isCreative() || player.inventory.contains(new ItemStack(Blocks.DIRT)))) {
                boolean canceled = ForgeEventFactory.onBlockPlace(player, BlockSnapshot.create(world.dimension(), world, aoePos), Direction.UP);

                if (!canceled && (player.isCreative() || consumeStack(new ItemStack(Blocks.DIRT), player.inventory))) {
                    world.setBlock(aoePos, Blocks.DIRT.defaultBlockState(), 3);
                }
            }

            ItemUseContext aoectx = new ItemUseContext(ctx.getPlayer(), ctx.getHand(), ctx.hitResult.withPosition(aoePos));
            boolean canDropAbove = world.getBlockState(aoePos.above()).getBlock() == Blocks.DIRT || world.getBlockState(aoePos.above()).getBlock() == Blocks.GRASS || world.getBlockState(aoePos.above()).getBlock() == Blocks.FARMLAND;
            boolean canRemoveAbove = canDropAbove ||
                    world.getBlockState(aoePos.above()).getBlock()
                            .canBeReplaced(world.getBlockState(aoePos.above()), new BlockItemUseContext(aoectx));
            boolean up2OK = world.isEmptyBlock(aoePos.above().above()) ||
                    world.getBlockState(aoePos.above().above()).getBlock()
                            .canBeReplaced(world.getBlockState(aoePos.above().above()), new BlockItemUseContext(aoectx));

            if (!world.isEmptyBlock(aoePos.above()) && canRemoveAbove && up2OK) {
                if (!world.isClientSide() && canDropAbove) {
                    world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY(), player.getZ(), new ItemStack(Blocks.DIRT)));
                }
                world.removeBlock(aoePos.above(), false);
            }
            attemptHoe(new ItemUseContext(ctx.getPlayer(), ctx.getHand(), aoectx.hitResult));
        }

//        AvaritiaEventHandler.stopItemCapture();
//        Set<ItemStack> drops = AvaritiaEventHandler.getCapturedDrops();
//        if (!world.isClientSide()) {
//            List<ItemStack> clusters = ItemMatterCluster.makeClusters(drops);
//            for (ItemStack cluster : clusters) {
//                ItemUtils.dropItem(world, origin.above(), cluster);
//            }
//        }

        return ActionResultType.SUCCESS;
    }

    public boolean consumeStack(ItemStack stack, IInventory inventory) {
        if (stack.isEmpty()) {
            return false;
        }

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack s = inventory.getItem(i);
            if (s.isEmpty()) {
                continue;
            }

            if (ItemStack.isSame(stack, s) && (!stack.hasTag() || !s.hasTag() || (stack.getTag().equals(s.getTag()))) && s.getCount() >= stack.getCount()) {
                s.shrink(stack.getCount());
                inventory.setChanged();
                return true;
            }
        }

        return false;
    }

    /**
     * Attempts to hoe a block.
     * Basically carbon copy of vanilla but not really.
     *
     * @param ctx the item use context
     * @return If the hoe operation was successful.
     */
    private boolean attemptHoe(ItemUseContext ctx) {
        World world = ctx.getLevel();
        BlockPos blockpos = ctx.getClickedPos();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(ctx);
        if (hook != 0) return hook > 0;
        if (ctx.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
            BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, ctx.getPlayer(), ctx.getItemInHand(), net.minecraftforge.common.ToolType.HOE);
            if (blockstate != null) {
                PlayerEntity playerentity = ctx.getPlayer();
                world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClientSide()) {
                    world.setBlock(blockpos, blockstate, 11);
                    if (playerentity != null) {
                        ctx.getItemInHand().hurtAndBreak(1, playerentity, (p_220043_1_) -> {
                            p_220043_1_.broadcastBreakEvent(ctx.getHand());
                        });
                    }
                }

                return true;
            }
        }

        return false;
    }

    // No longer needed because hoeitem changed lesgo!
//    @Override
//    protected void setBlock(ItemStack stack, PlayerEntity player, World worldIn, BlockPos pos, BlockState state) {
//        worldIn.playSound(player, pos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
//
//        if (!worldIn.isClientSide()) {
//            worldIn.setBlock(pos, state, 0b1011);
//        }
//    }

    @Override // TODO: not todo but remeber note
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
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
}
