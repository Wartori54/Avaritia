package morph.avaritia.item.tools;

import codechicken.lib.raytracer.RayTracer;
import morph.avaritia.Avaritia;
import morph.avaritia.api.InfinityItem;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.ModHelper;
import morph.avaritia.util.ToolHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.common.ToolType;

public class ItemShovelInfinity extends ShovelItem  implements InfinityItem {

    //private IIcon destroyer;

    public ItemShovelInfinity() {
        super(ModHelper.InfinityTier.INFINITY_TIER, 1.5F, -3.0F, new Properties().stacksTo(1).tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY).defaultDurability(9999));
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getTag() != null && stack.getTag().getBoolean("destroyer")) {
            return 5.0F;
        }
        for (ToolType type : getToolTypes(stack)) {
            if (state.getBlock().isToolEffective(state, type)) {
                return speed;
            }
        }
        return Math.max(super.getDestroySpeed(stack, state), 1.0F);
    }

    //@SideOnly(Side.CLIENT)
    //public void registerIcons(IIconRegister ir) {
    //    this.itemIcon = ir.registerIcon("avaritia:infinity_shovel");
    //    destroyer = ir.registerIcon("avaritia:infinity_destroyer");
    //}

    //@Override
    //public IIcon getIcon(ItemStack stack, int pass){
    //    NBTTagCompound tags = stack.getTagCompound();
    //    if(tags != null){
    //        if(tags.getBoolean("destroyer"))
    //            return destroyer;
    //    }
    //    return itemIcon;
    //}

    //@SideOnly(Side.CLIENT)
    //@Override
    //public IIcon getIconIndex(ItemStack stack){
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
            tags.putBoolean("destroyer", !tags.getBoolean("destroyer"));
            player.swing(hand);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        if (player.isCreative() && itemstack.getOrCreateTag().getBoolean("destroyer")) {
            BlockRayTraceResult traceResult = RayTracer.retrace(player,
                    10, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
            if (traceResult != null) {
                breakOtherBlock(player, itemstack, pos, traceResult.getDirection());
            }
        }
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity player) {
        if (stack.getTag() != null && stack.getTag().getBoolean("destroyer") && player instanceof PlayerEntity) {
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
        BlockPos min = new BlockPos(-range, doY ? -1 : -range, -range);
        BlockPos max = new BlockPos(range, doY ? range * 2 - 2 : range, range);

        ToolHelper.aoeBlocks(player, stack, world, pos, min, max, null, ItemPickaxeInfinity.MATERIALS, false);

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
        return ItemPickaxeInfinity.MATERIALS.contains(material);
    }
}
