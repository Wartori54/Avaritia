package morph.avaritia.tile;

import codechicken.lib.packet.PacketCustom;
import morph.avaritia.container.ContainerNeutronCollector;
import morph.avaritia.init.ModContent;
import morph.avaritia.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class TileNeutronCollector extends TileMachineBase implements IInventory {

    public static final int PRODUCTION_TICKS = 7111;//TODO config.

    private ItemStack neutrons = ItemStack.EMPTY;
    private int progress;
    private IIntArray dataAccess = new IIntArray() {
        @Override
        public int get(int id) {
            switch (id) {
                case 0:
                    return TileNeutronCollector.this.getProgress();
                case 1:
                    return TileNeutronCollector.PRODUCTION_TICKS;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int id, int value) {
            switch (id) {
                case 0:
                    TileNeutronCollector.this.progress = value;
                    break;
                case 1: // cannot change processing time as its final
                default:
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private final LazyOptional<IItemHandler> inventoryHandlerLazyOptional = LazyOptional.of(() -> new InvWrapper(this));

    public TileNeutronCollector() {
        super(ModContent.tileNeutronCollector);
    }

    @Override
    public void doWork() {
        if (++progress >= PRODUCTION_TICKS) {
            if (neutrons.isEmpty()) {
                neutrons = new ItemStack(ModItems.neutron_pile, 1);
            } else if (neutrons.getItem().equals(ModItems.neutron_pile)) {
                if (neutrons.getCount() < getMaxStackSize()) {
                    neutrons.grow(1);
                }
            }
            progress = 0;
            setChanged();
        }
    }

    @Override
    protected void onWorkStopped() {
        progress = 0;
    }

    @Override
    protected boolean canWork() {
        return neutrons.isEmpty() || neutrons.getCount() < getMaxStackSize();
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        if (tag.contains("Neutrons")) {
            neutrons = ItemStack.of(tag.getCompound("Neutrons"));
        }
        progress = tag.getInt("Progress");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putInt("Progress", progress);
        if (neutrons != null) {
            CompoundNBT produce = new CompoundNBT();
            neutrons.save(produce);
            tag.put("Neutrons", produce);
        } else {
            tag.remove("Neutrons");
        }
        return super.save(tag);
    }

    @Override
    public void writeGuiData(PacketCustom packet, boolean isFullSync) {
        packet.writeVarInt(progress);
    }

    @Override
    public void readGuiData(PacketCustom packet, boolean isFullSync) {
        progress = packet.readVarInt();
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
//        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventoryHandlerLazyOptional.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        inventoryHandlerLazyOptional.invalidate();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return neutrons.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == 0) return neutrons;
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int decrement) {
        if (neutrons.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            if (decrement < neutrons.getCount()) {
                ItemStack take = neutrons.split(decrement);
                if (neutrons.getCount() <= 0) {
                    neutrons = ItemStack.EMPTY;
                }
                return take;
            } else {
                ItemStack take = neutrons;
                neutrons = ItemStack.EMPTY;
                return take;
            }
        }
    }

    @Override
    public void startOpen(PlayerEntity player) {
    }

    @Override
    public void stopOpen(PlayerEntity player) {
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double)this.worldPosition.getX() + 0.5D,
                    (double)this.worldPosition.getY() + 0.5D,
                    (double)this.worldPosition.getZ() + 0.5D)
                    > 64.0D);
        }
    }

//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack stack) {
//        return false;
//    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != 0) return;
        neutrons = stack;
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("container.avaritia.neutron_collector");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) { // TODO: maybe wrong????
        return ItemStack.EMPTY;
    }

    @Override
    public void clearContent() {
    }

    @Override
    public ITextComponent getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new ContainerNeutronCollector(id, playerInv, this, this.dataAccess);
    }
}
