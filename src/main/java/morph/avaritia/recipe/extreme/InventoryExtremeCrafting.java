package morph.avaritia.recipe.extreme;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class InventoryExtremeCrafting extends CraftingInventory {

    private final IInventory craft;
    private final Container container;

    public InventoryExtremeCrafting(Container cont, IInventory table) {
        super(cont, 9, 9);
        craft = table;
        container = cont;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= craft.getContainerSize() ? ItemStack.EMPTY : craft.getItem(slot + 1);
    }

    // this is no longer used in minecraft but i'll keep it cuz it's useful
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < 9) {
            int x = row + column * 9;
            return getItem(x);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItem(int slot, int decrement) {
        ItemStack stack = craft.getItem(slot + 1);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= decrement) {
                itemstack = stack.copy();
                craft.setItem(slot + 1, ItemStack.EMPTY);
            } else {
                itemstack = stack.split(decrement);
                if (stack.getCount() == 0) {
                    craft.setItem(slot + 1, ItemStack.EMPTY);
                }
            }
            container.slotsChanged(this);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int slot, ItemStack itemstack) {
        craft.setItem(slot + 1, itemstack);
        container.slotsChanged(this);
    }

}
