package morph.avaritia.tile.capability;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class CapabilityAvaritiaMachine implements IItemHandler {

    private final IInventory machine;

    public CapabilityAvaritiaMachine(IInventory machine) {
        this.machine = machine;
    }

    @Override
    public int getSlots() {
        return machine.getContainerSize();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return machine.getItem(slot).copy(); // preventing modifications
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ItemStack currStack = getStackInSlot(slot);
        if (!currStack.sameItem(stack)) {
            return stack;
        }
        int amount = currStack.getCount() + stack.getCount();
        if (amount > getSlotLimit(slot)) {
            if (!simulate) {
                machine.setItem(slot, new ItemStack(currStack.getItem(), getSlotLimit(slot)));
            }
            return new ItemStack(currStack.getItem(), amount-getSlotLimit(slot));
        } else {
            if (!simulate) {
                machine.setItem(slot, new ItemStack(currStack.getItem(), amount));
            }
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (amount > stack.getCount()) {
            if (!simulate) {
                machine.removeItem(slot, amount);
            }
            return stack.copy();
        } else {
            if (!simulate) {
                machine.removeItem(slot, amount);
            }
            return new ItemStack(stack.getItem(), amount);

        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return machine.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return machine.getItem(slot).sameItem(stack) &&
               machine.getItem(slot).getCount() + stack.getCount() < getSlotLimit(slot);
    }
}
