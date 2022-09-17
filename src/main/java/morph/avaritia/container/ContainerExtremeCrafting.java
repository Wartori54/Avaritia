package morph.avaritia.container;

import morph.avaritia.init.ModContent;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.InventoryExtremeCraftResult;
import morph.avaritia.recipe.extreme.InventoryExtremeCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ContainerExtremeCrafting extends Container {

    /**
     * The crafting matrix inventory (9x9).
     */
    public CraftingInventory craftMatrix;
    public InventoryExtremeCraftResult craftResult;
    protected IInventory container;

    public ContainerExtremeCrafting(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
        this(windowId, playerInv, new Inventory(81 + 1));
    }

    public ContainerExtremeCrafting(int windowId, PlayerInventory player, IInventory table) {
        super(ModContent.containerExtremeCrafting, windowId);
        checkContainerSize(table, 81 + 1);
        this.container = table;
        this.craftMatrix = new InventoryExtremeCrafting(this, table);
        this.craftResult = new InventoryExtremeCraftResult(table);
        this.addSlot(new SlotExtremeCrafting(player.player, craftMatrix, craftResult, 0, 210, 80));
        int wy;
        int ex;

        for (wy = 0; wy < 9; ++wy) {
            for (ex = 0; ex < 9; ++ex) {
                addSlot(new Slot(craftMatrix, ex + wy * 9, 12 + ex * 18, 8 + wy * 18));
            }
        }

        for (wy = 0; wy < 3; ++wy) {
            for (ex = 0; ex < 9; ++ex) {
                addSlot(new Slot(player, ex + wy * 9 + 9, 39 + ex * 18, 174 + wy * 18));
            }
        }

        for (ex = 0; ex < 9; ++ex) {
            addSlot(new Slot(player, ex, 39 + ex * 18, 232));
        }

        slotsChanged(craftMatrix);
    }

    @Override
    public void slotsChanged(IInventory matrix) {
        craftResult.setItem(0, AvaritiaRecipeManager.getExtremeCraftingResult(craftMatrix));
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);

    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = getSlot(slotNumber);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!moveItemStackTo(itemstack1, 82, 118, true)) {//83 start??
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotNumber >= 82 && slotNumber < 109) {
                if (!moveItemStackTo(itemstack1, 109, 118, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotNumber >= 109 && slotNumber < 118) {
                if (!moveItemStackTo(itemstack1, 82, 109, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemstack1, 82, 118, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

//    @Override // idk why is this here
//    public boolean stillValid(PlayerEntity p_75145_1_) {
//        return false;
//    }
}
