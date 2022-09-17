package morph.avaritia.container;

import morph.avaritia.container.slot.OutputSlot;
import morph.avaritia.init.ModContent;
import morph.avaritia.tile.TileNeutronCollector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.Point;

public class ContainerNeutronCollector extends ContainerMachineBase {

    private final IIntArray data;

    public ContainerNeutronCollector(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
        this(windowId, playerInv, new Inventory(1), new IntArray(2));
    }

    public ContainerNeutronCollector(int windowId, PlayerInventory playerInventory, IInventory machine, IIntArray data) {
        super(ModContent.containerNeutronCollector, windowId, machine);
        checkContainerSize(machine, 1);
        checkContainerDataCount(data, 2);
        this.data = data;
        addSlot(new OutputSlot(machine, 0, 80, 35));
        this.addDataSlots(data);

        bindPlayerInventory(playerInventory);
    }

    @Override
    protected Point getPlayerInvOffset() {
        return new Point(8, 84);
    }

    public ItemStack quickMoveStack(PlayerEntity player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(slotNumber);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!moveItemStackTo(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (slotNumber >= 1 && slotNumber < 28) {
                    if (!moveItemStackTo(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNumber >= 28 && slotNumber < 37 && !moveItemStackTo(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
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

    public int getProgress() {
        return this.data.get(0);
    }

    public int getTotalProgress() {
        return this.data.get(1);
    }
}
