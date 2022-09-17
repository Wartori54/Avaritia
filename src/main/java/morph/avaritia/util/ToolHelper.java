/**
 * This class based on code by Vazkii in the Botania mod.
 *
 * Get the original source here:
 * https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/tool/ToolCommons.java
 */

package morph.avaritia.util;

import codechicken.lib.util.ItemUtils;
import com.google.common.collect.Sets;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.ItemMatterCluster;
import morph.avaritia.item.tools.ItemAxeInfinity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import java.util.*;
import java.util.Map.Entry;

public class ToolHelper {

    public static Material[] materialsPick = new Material[] { Material.STONE, Material.METAL, Material.ICE,
            Material.GLASS, Material.PISTON, Material.HEAVY_METAL };
    public static Material[] materialsShovel = new Material[] { Material.GRASS, Material.DIRT, Material.SAND,
            Material.SNOW, Material.TOP_SNOW, Material.CLAY };


    public static void aoeBlocks(PlayerEntity player, ItemStack stack, World world, BlockPos origin, BlockPos min, BlockPos max, Block target, Set<Material> validMaterials, boolean filterTrash) {

        if (world.isClientSide()) return;
//        AvaritiaEventHandler.enableItemCapture();
        if (stack.getItem() instanceof ItemAxeInfinity)
            ItemAxeInfinity.setRuining(stack, true);


        for (int lx = min.getX(); lx < max.getX(); lx++) {
            for (int ly = min.getY(); ly < max.getY(); ly++) {
                for (int lz = min.getZ(); lz < max.getZ(); lz++) {
                    BlockPos pos = origin.offset(lx, ly, lz);
                    removeBlockWithDrops(player, stack, world, pos, target, validMaterials);
                }
            }
        }

//        AvaritiaEventHandler.stopItemCapture();
//        Set<ItemStack> drops = AvaritiaEventHandler.getCapturedDrops();
        if (stack.getItem() instanceof ItemAxeInfinity)
            ItemAxeInfinity.setRuining(stack, false);
        List<ItemStack> drops = MatterClusterModifier.flush(stack);
        if (filterTrash) {
            drops = removeTrash(stack, drops);
        }
        if (!world.isClientSide()) {
            List<ItemStack> clusters = ItemMatterCluster.makeClusters(drops);
            for (ItemStack cluster : clusters) {
                ItemUtils.dropItem(world, origin, cluster);
            }
        }

    }

    public static void removeBlockWithDrops(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Block target, Set<Material> validMaterials) {
        if (!world.isLoaded(pos)) {
            return;
        }
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!world.isClientSide()) {
            if ((target != null && target != state.getBlock()) || block.isAir(state, world, pos)) {
                return;
            }
            Material material = state.getMaterial();
            if (stack.getItem() == ModItems.infinity_axe && ItemAxeInfinity.axeEditBlocks.containsKey(block)) {
                world.setBlock(pos, ItemAxeInfinity.axeEditBlocks.get(block).defaultBlockState(), 3); // 3 means Block.UPDATE_ALL
                return;
            }
            if (!block.canHarvestBlock(world.getBlockState(pos), world, pos, player) || !validMaterials.contains(material)) {
                return;
            }
            BreakEvent event = new BreakEvent(world, pos, state, player);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                if (!player.isCreative()) {
                    TileEntity tile = world.getBlockEntity(pos);
                    block.playerWillDestroy(world, pos, state, player);
                    if (block.removedByPlayer(state, world, pos, player, true, state.getFluidState())) {
                        world.removeBlock(pos, false);
                        block.playerDestroy(world, player, pos, state, tile, stack);
                    }
                } else {
                    world.removeBlock(pos, false);
                }
            }
        }
    }

    public static List<ItemStack> removeTrash(ItemStack holdingStack, List<ItemStack> drops) {
        Set<ItemStack> trashItems = new HashSet<>();
        for (ItemStack drop : drops) {
            if (isTrash(holdingStack, drop)) {
                //Lumberjack.info("Removing: " + drop.toString());
                trashItems.add(drop);
            }
        }
        drops.removeAll(trashItems);
        return drops;
    }

    private static boolean isTrash(ItemStack holdingStack, ItemStack suspect) {
        return AvaritiaEventHandler.defaultTrashOres.contains(suspect.getItem().getRegistryName());
    }

    public static List<ItemStack> collateDropList(List<ItemStack> input) {
        return collateMatterClusterContents(collateMatterCluster(input));
    }

    public static List<ItemStack> collateMatterClusterContents(Map<ItemStackWrapper, Integer> input) {
        List<ItemStack> collated = new ArrayList<>();

        for (Entry<ItemStackWrapper, Integer> e : input.entrySet()) {
            int count = e.getValue();
            ItemStackWrapper wrap = e.getKey();

            int size = wrap.stack.getMaxStackSize();
            int fullstacks = (int) Math.floor((float) count / size);

            for (int i = 0; i < fullstacks; i++) {
                count -= size;
                ItemStack stack = wrap.stack.copy();
                stack.setCount(size);
                collated.add(stack);
            }

            if (count > 0) {
                ItemStack stack = wrap.stack.copy();
                stack.setCount(count);
                collated.add(stack);
            }
        }

        return collated;
    }

    public static Map<ItemStackWrapper, Integer> collateMatterCluster(List<ItemStack> input) {
        Map<ItemStackWrapper, Integer> counts = new HashMap<>();

        if (input != null) {
            for (ItemStack stack : input) {
                ItemStackWrapper wrap = new ItemStackWrapper(stack);
                if (!counts.containsKey(wrap)) {
                    counts.put(wrap, 0);
                }

                counts.put(wrap, counts.get(wrap) + stack.getCount());
            }
        }

        return counts;
    }
}
