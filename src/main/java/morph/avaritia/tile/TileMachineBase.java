package morph.avaritia.tile;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.CommonUtils;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.network.NetworkDispatcher;
import morph.avaritia.util.ModHelper;
import morph.avaritia.util.TimeTracker;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by covers1624 on 20/05/2017.
 */
public abstract class TileMachineBase extends TileBase implements ITickableTileEntity, INamedContainerProvider, INameable {

    //The machine is currently doing stuff.
    protected boolean isActive;
    //The machine was doing stuff and a client update is pending.
    private boolean wasActive;
//    Tracks how long the machine has been off for, and then triggers an update after x time.
//    protected TimeTracker offTracker = new TimeTracker();

//    private boolean sendUpdatePacket;
    public boolean fullContainerSync;

    protected Direction facing = Direction.NORTH;

    public TileMachineBase(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public final void tick() {
        if (CommonUtils.isClientWorld(this.level)) {
            return;
        }

        if (canWork()) {
            isActive = true;
            doWork();
        } else {
            isActive = false;
            if (wasActive) {
                onWorkStopped();
            }
        }

//        if (canWork()) {
//            if (!isActive && !wasActive) {
//                sendUpdatePacket = true;
//            }
//            isActive = true;
//            wasActive = false;
//            doWork();
//        } else {
//            if (isActive) {
//                onWorkStopped();
//                wasActive = true;
//                if (this.level != null) {
//                    offTracker.markTime(this.level);
//                }
//            }
//            isActive = false;
//        }
        if (isActive != wasActive) {
            this.level.setBlock(getBlockPos(), this.level.getBlockState(getBlockPos()).setValue(ModHelper.ACTIVE, isActive), 3);
            wasActive = isActive;
        }
//        updateCheck();
    }

//    /**
//     * Does checks to see if a delay has passed since the machine was turned off for triggering client updates.
//     */
//    private void updateCheck() {
//        if (wasActive && offTracker.hasDelayPassed(this.level, 100)) {
//            wasActive = false;
//            sendUpdatePacket = true;
//        }
//        if (sendUpdatePacket) {
//            sendUpdatePacket = false;
////            NetworkDispatcher.dispatchMachineUpdate(this);
//        }
//    }

    /**
     * Called on the server to write data to the update packet.
     *
     * @param packet The packet to write data to.
     */
    public void writeUpdateData(PacketCustom packet) {
        packet.writeBoolean(isActive);
        packet.writeByte(facing.get3DDataValue());
    }

    /**
     * Called on the client to read the update data from the server.
     *
     * @param packet The packet to read data from.
     */
    public void readUpdatePacket(PacketCustom packet) {
        isActive = packet.readBoolean();
        facing = Direction.values()[packet.readUByte()];
        BlockState state = this.level.getBlockState(getBlockPos());
        this.level.sendBlockUpdated(getBlockPos(), state, state, 3);
    }

    public abstract void writeGuiData(PacketCustom packet, boolean isFullSync);

    public abstract void readGuiData(PacketCustom packet, boolean isFullSync);

//    @Override // TODO: check this
//    public void load(BlockState state, CompoundNBT compound) {
//        super.load(state, compound);
//        facing = Direction.values()[compound.getByte("facing")];
//    }

//    @Override
//    public CompoundNBT save(CompoundNBT compound) {
//        compound.putByte("facing", (byte) facing.ordinal());
//        return super.save(compound);
//    }

//    @Override
//    public CompoundNBT getUpdateTag() {
//        CompoundNBT tag = super.getUpdateTag();
//        tag.putBoolean("active", isActive);
//        return tag;
//    }

//    @Override
//    public void handleUpdateTag(NBTTagCompound tag) {
//        super.handleUpdateTag(tag);
//        isActive = tag.getBoolean("active");
//    }

    /**
     * Called Server side to check if work can happen.
     *
     * @return If work can happen.
     */
    protected abstract boolean canWork();

    /**
     * Called server side to do work.
     */
    protected abstract void doWork();

    protected abstract void onWorkStopped();

//    public Direction getFacing() {
//        return facing;
//    }

//    public void setFacing(EnumFacing facing) {
//        this.facing = facing;
//    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public abstract ITextComponent getDisplayName();
}
