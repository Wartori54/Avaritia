package morph.avaritia.container;

import morph.avaritia.container.slot.OutputSlot;
import morph.avaritia.container.slot.ScrollingFakeSlot;
import morph.avaritia.container.slot.StaticFakeSlot;
import morph.avaritia.init.ModContent;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.tile.TileMachineBase;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ContainerNeutroniumCompressor extends ContainerMachineBase {

    public OutputSlot outputSlot;

    public ContainerNeutroniumCompressor(int windowId, PlayerInventory playerInv, PacketBuffer extraData) {
        this(windowId, playerInv, new Inventory(2));
    }

    public ContainerNeutroniumCompressor(int windowId, PlayerInventory playerInv, IInventory machine) {
        super(ModContent.containerNeutroniumCompressor, windowId, machine);
        checkContainerSize(machine, 2);
        addSlot(new Slot(machine, 0, 39, 35));
        addSlot(outputSlot = new OutputSlot(machine, 1, 117, 35));
        bindPlayerInventory(playerInv);
        addSlot(new StaticFakeSlot(147, 35, () -> {
            if (machine instanceof TileNeutroniumCompressor) {
                return ((TileNeutroniumCompressor) machine).getTargetStack();
            } else {
                return ItemStack.EMPTY;
            }
        }));
        addSlot(new ScrollingFakeSlot(13, 35, () -> {
            if (machine instanceof TileNeutroniumCompressor) {
                return ((TileNeutroniumCompressor) machine).getInputItems();
            } else {
                return Collections.emptyList();
            }
        }));
    }

    @Override
    protected Point getPlayerInvOffset() {
        return new Point(8, 84);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack quickMoveStack(PlayerEntity player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = getSlot(slotNumber);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (slotNumber == 1) {
                if (!moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotNumber != 0) {
                if (AvaritiaRecipeManager.getCompressorRecipeFromInput(itemstack1) != null) {
                    if (!moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNumber >= 2 && slotNumber < 29) {
                    if (!moveItemStackTo(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNumber >= 29 && slotNumber < 38 && !moveItemStackTo(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemstack1, 2, 38, false)) {
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
}
