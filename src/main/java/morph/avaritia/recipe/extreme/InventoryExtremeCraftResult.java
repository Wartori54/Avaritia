package morph.avaritia.recipe.extreme;

import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryExtremeCraftResult extends CraftResultInventory {

    private IInventory craft;

    public InventoryExtremeCraftResult(IInventory table) {
        craft = table;
    }

    @Override
    public ItemStack getItem(int par1) {
        return craft.getItem(0);
    }

    @Override
    public ItemStack removeItem(int par1, int par2) {
        ItemStack stack = craft.getItem(0);
        if (!stack.isEmpty()) {
            craft.setItem(0, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int par1, ItemStack par2ItemStack) {
        craft.setItem(0, par2ItemStack);
    }

}
