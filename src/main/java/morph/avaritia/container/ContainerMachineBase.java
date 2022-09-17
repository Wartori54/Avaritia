package morph.avaritia.container;

import morph.avaritia.network.NetworkDispatcher;
import morph.avaritia.tile.TileMachineBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;

import javax.annotation.Nullable;
import java.awt.*;


/**
 * Created by covers1624 on 20/05/2017.
 */
public abstract class ContainerMachineBase extends Container {

    @Nullable
    protected final IInventory machineTile;
    //Internal flag for the first packet set when the gui is opened.
    private boolean isFirstPacket;

    public ContainerMachineBase(ContainerType<?> machine, int windowId, IInventory machineTile) {
        super(machine, windowId);
        this.machineTile = machineTile;
    }

    protected abstract Point getPlayerInvOffset();

    protected void bindPlayerInventory(PlayerInventory playerInventory) {

        Point offset = getPlayerInvOffset();
        int xOffset = offset.x;
        int yOffset = offset.y;

        for (int row = 0; row < 3; ++row) {
            for (int slot = 0; slot < 9; ++slot) {
                addSlot(new Slot(playerInventory, slot + row * 9 + 9, xOffset + slot * 18, yOffset + row * 18));
            }
        }

        for (int slot = 0; slot < 9; ++slot) {
            addSlot(new Slot(playerInventory, slot, xOffset + slot * 18, yOffset + 58));
        }
    }

    public IInventory getTile() {
        return machineTile;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return machineTile != null && machineTile.stillValid(playerIn);
    }

    /*
        This is no longer needed for 1.16 as content updating is automatically done by minecraft
     */
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//        if (machineTile instanceof TileMachineBase) {
//            for (IContainerListener listener : containerListeners) {
//                if (listener instanceof PlayerEntity) {
//                    NetworkDispatcher.dispatchGuiChanges(((PlayerEntity) listener), (TileMachineBase) machineTile, isFirstPacket | ((TileMachineBase) machineTile).fullContainerSync);
//                }
//            }
//            ((TileMachineBase) machineTile).fullContainerSync = false;
//        }
//        isFirstPacket = false;
//    }
}
