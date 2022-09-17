package morph.avaritia.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by covers1624 on 22/05/2017.
 */
public class OutputSlot extends Slot {

    public OutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nullable ItemStack stack) {
        return false;
    }
}
