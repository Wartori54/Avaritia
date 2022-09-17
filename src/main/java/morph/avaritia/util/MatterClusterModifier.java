package morph.avaritia.util;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import morph.avaritia.api.InfinityItem;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.tools.ItemAxeInfinity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.*;

public class MatterClusterModifier extends LootModifier {

    public static final Map<ItemStack, LinkedList<ItemStack>> collectedDrops = new HashMap<>();
    private static final Set<Item> affectedTools = Sets.newHashSet(ModItems.infinity_pickaxe, ModItems.infinity_shovel, ModItems.infinity_axe);


    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected MatterClusterModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        ItemStack tool = context.getParamOrNull(LootParameters.TOOL);
        Entity player = context.getParamOrNull(LootParameters.THIS_ENTITY);
        if (tool != null && affectedTools.contains(tool.getItem()) && isValid(tool, context.getParamOrNull(LootParameters.BLOCK_STATE))) {
            if (!collectedDrops.containsKey(tool)) {
                collectedDrops.put(tool, new LinkedList<>());
            }
            collectedDrops.get(tool).addAll(generatedLoot);
            return Collections.emptyList();
        } else {
            return generatedLoot;
        }
    }

    private boolean isValid(ItemStack tool, BlockState blockState) {
        if (!tool.hasTag() || !(tool.getItem() instanceof InfinityItem)) return false;
        boolean validMat = ((InfinityItem) tool.getItem()).isValidMaterial(blockState.getMaterial());
        boolean validTag = tool.getTag().getBoolean("hammer") || tool.getTag().getBoolean("destroyer");
        boolean isValidAxe = tool.getItem() instanceof ItemAxeInfinity && tool.hasTag() && tool.getTag().getBoolean("is_ruining");
        return validMat && (validTag || isValidAxe);
    }

    public static List<ItemStack> flush(ItemStack stack) {
        if (!collectedDrops.containsKey(stack)) {
            Lumberjack.log(Level.INFO, "stack doesn't match for drops, or no drops generated, safe to ignore");
            return Collections.emptyList();
        } else {
            List<ItemStack> drops = collectedDrops.get(stack);
            collectedDrops.remove(stack);
            return drops;
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<MatterClusterModifier> {

        @Override
        public MatterClusterModifier read(ResourceLocation location, JsonObject object, ILootCondition[] lootConditions) {
            return new MatterClusterModifier(lootConditions);
        }

        @Override
        public JsonObject write(MatterClusterModifier instance) {
            return makeConditions(instance.conditions);
        }
    }

}
