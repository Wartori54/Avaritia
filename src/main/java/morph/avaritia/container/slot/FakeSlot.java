package morph.avaritia.container.slot;

import codechicken.lib.inventory.InventorySimple;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Created by covers1624 on 22/05/2017.
 */
public class FakeSlot extends OutputSlot {

    public FakeSlot(int xPosition, int yPosition) {
        super(new InventorySimple(1), 0, xPosition, yPosition);

    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Blocks.NETHER_BRICKS);
    }

    @Override
    public void set(ItemStack stack) {
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        return false;
    }

    //    @Override
    //    public boolean canBeHovered() {
    //        return getHasStack();
    //    }
}
