package morph.avaritia.handler;

import morph.avaritia.item.tools.ItemAxeInfinity;
import morph.avaritia.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class AEOCrawlerTask {

    final World world;
    final PlayerEntity player;
    final ItemStack stack;
    final int steps;
    final BlockPos origin;
    final boolean leaves;
    final boolean force;
    final Set<BlockPos> posChecked;

    AEOCrawlerTask(World world, PlayerEntity player, ItemStack stack, BlockPos origin, int steps, boolean leaves, boolean force, Set<BlockPos> posChecked) {
        this.world = world;
        this.player = player;
        this.stack = stack;
        this.origin = origin;
        this.steps = steps;
        this.leaves = leaves;
        this.force = force;
        this.posChecked = posChecked;
    }

    void tick() {
        BlockState originState = world.getBlockState(origin);
        Block originBlock = originState.getBlock();
        if (!force && originBlock.isAir(originState, world, origin)) {
            return;
        }
        ToolHelper.removeBlockWithDrops(player, stack, world, origin, null, ItemAxeInfinity.MATERIALS);
        if (steps == 0) {
            return;
        }
        for (Direction dir : Direction.values()) {
            BlockPos stepPos = origin.offset(dir.getNormal());
            if (posChecked.contains(stepPos)) {
                continue;
            }
            BlockState stepState = world.getBlockState(stepPos);
            Block stepBlock = stepState.getBlock();
            boolean log = stepState.getMaterial() == Material.WOOD && stepBlock instanceof RotatedPillarBlock;
            boolean leaf = stepState.getMaterial() == Material.LEAVES;
            if (log || leaf) {
                int steps = this.steps - 1;
                steps = leaf ? (leaves ? steps : 3) : steps;
                AvaritiaEventHandler.startCrawlerTask(world, player, stack, stepPos, steps, leaf, false, posChecked);
                posChecked.add(stepPos);
            }
        }
    }
}
