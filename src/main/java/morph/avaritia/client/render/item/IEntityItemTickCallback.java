package morph.avaritia.client.render.item;

import net.minecraft.entity.item.ItemEntity;

public interface IEntityItemTickCallback {

    void onEntityTick(ItemEntity item);
}
