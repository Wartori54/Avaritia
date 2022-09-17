package morph.avaritia.client.render.item;

import codechicken.lib.math.MathHelper;
import codechicken.lib.model.bakery.CCBakeryModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Created by covers1624 on 17/04/2017.
 */
public class InfinityBowModelWrapper extends CCBakeryModel {

    private final ItemOverrideList superList = super.getOverrides();
    private final ItemOverrideList wrappedList = new WrappedBowOverrideList();

    public InfinityBowModelWrapper() {
        super();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return wrappedList;
    }

    private class WrappedBowOverrideList extends ItemOverrideList {

        public WrappedBowOverrideList() {

        }

        @Override
        public IBakedModel resolve(IBakedModel originalModel, ItemStack stack, ClientWorld world, LivingEntity entity) {
            if (entity != null) {
                stack = stack.copy();
                stack.getOrCreateTag().putInt("frame", getBowFrame(entity));
            }
            return superList.resolve(originalModel, stack, world, entity);
        }

    }

    public static int getBowFrame(LivingEntity entity) {
        ItemStack inuse = entity.getUseItem();
        int time = entity.getUseItemRemainingTicks();

        if (!inuse.isEmpty()) {
            int max = inuse.getUseDuration();
            double pull = (max - time) / (double) max;
            int frame = Math.max(0, (int) Math.ceil(pull * 3.0) - 1);
            frame = MathHelper.clip(frame, 0, 2);
            return frame;
        }
        return -1;
    }
}
