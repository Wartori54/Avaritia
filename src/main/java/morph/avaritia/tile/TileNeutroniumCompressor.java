package morph.avaritia.tile;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.ItemUtils;
import morph.avaritia.container.ContainerNeutroniumCompressor;
import morph.avaritia.init.ModContent;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class TileNeutroniumCompressor extends TileMachineBase implements ISidedInventory {

    //Number of ticks needed to consume an item.
    public static int CONSUME_TICKS = 1;//TODO Config.

    //The consumption progress.
    private int consumption_progress;
    //The production progress.
    private int compression_progress;
    //What we are creating.
    private ItemStack target_stack = ItemStack.EMPTY;
    private int compression_target;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;

    private List<ItemStack> c_InputItems;

    private static final int[] top = new int[] { 0 };
    private static final int[] sides = new int[] { 1 };

    private static final Direction[] allSides = {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, null};

    private final LazyOptional<IItemHandlerModifiable>[] inventoryHandlerLazyOptional = SidedInvWrapper.create(this, allSides);

    public TileNeutroniumCompressor() {
        super(ModContent.tileNeutroniumCompressor);
    }

    @Override
    public void doWork() {

        boolean dirty = false;

        if (target_stack.isEmpty()) {
            fullContainerSync = true;
            ICompressorRecipe recipe = AvaritiaRecipeManager.getCompressorRecipeFromInput(input);
            target_stack = recipe.getResult();
            compression_target = recipe.getCost();
        }

        consumption_progress++;
        if (consumption_progress == CONSUME_TICKS) {
            consumption_progress = 0;

            input.shrink(1);
            if (input.getCount() == 0) {
                input = ItemStack.EMPTY;
            }

            compression_progress++;
            dirty = true;
        }

        if (compression_progress >= compression_target) {
            compression_progress = 0;
            if (output.isEmpty()) {
                output = ItemUtils.copyStack(target_stack, 1);
            } else {
                output.grow(1);
            }
            dirty = true;
            target_stack = ItemStack.EMPTY;
            fullContainerSync = true;
        }

        if (dirty) {
            setChanged();
        }
    }

    @Override
    protected void onWorkStopped() {
        consumption_progress = 0;
    }

    @Override
    protected boolean canWork() {
        if (input.isEmpty()) {
            return false;
        }
        if(!target_stack.isEmpty()) {
            ICompressorRecipe recipe = AvaritiaRecipeManager.getCompressorRecipeFromResult(target_stack);
            return recipe.matches(input);
        }
        ICompressorRecipe recipe = AvaritiaRecipeManager.getCompressorRecipeFromInput(input);
        return recipe != null && (output.isEmpty() || (recipe.getResult().sameItem(output) && output.getCount() < Math.min(output.getMaxStackSize(), getMaxStackSize())));
    }

    @Override
    public void writeGuiData(PacketCustom packet, boolean isFullSync) {
        packet.writeVarInt(consumption_progress);
        packet.writeVarInt(compression_progress);

        if (isFullSync) {
            packet.writeBoolean(!target_stack.isEmpty());
            if (!target_stack.isEmpty()) {
                packet.writeVarInt(compression_target);
                packet.writeItemStack(target_stack);
            }

            List<Ingredient> ings = Collections.emptyList();
            if (!target_stack.isEmpty()) {
                ings = AvaritiaRecipeManager.getCompressorRecipeFromResult(target_stack).getIngredients();
            }
            List<ItemStack> inputs = ings.stream().flatMap(l -> Arrays.stream(l.getItems())).collect(Collectors.toList());

            packet.writeInt(inputs.size());
            for (ItemStack input : inputs) {
                packet.writeItemStack(input);
            }
        }
    }

    @Override
    public void readGuiData(PacketCustom packet, boolean isFullSync) {
        consumption_progress = packet.readVarInt();
        compression_progress = packet.readVarInt();
        if (isFullSync) {
            if (packet.readBoolean()) {
                compression_target = packet.readVarInt();
                target_stack = packet.readItemStack();
            } else {
                target_stack = ItemStack.EMPTY;
                compression_target = 0;
            }

            List<ItemStack> inputs = new LinkedList<>();
            int items = packet.readInt();
            for (int i = 0; i < items; i++) {
                inputs.add(packet.readItemStack());
            }

            c_InputItems = inputs;
        }
    }

    public int getCompressionProgress() {
        return compression_progress;
    }

    public int getCompressionTarget() {
        return compression_target;
    }

    public int getConsumptionProgress() {
        return consumption_progress;
    }

    public int getConsumptionTarget() {
        return CONSUME_TICKS;
    }

    public ItemStack getTargetStack() {
        return target_stack;
    }

    public List<ItemStack> getInputItems() {
        return c_InputItems;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        input = ItemStack.of(tag.getCompound("input"));
        output = ItemStack.of(tag.getCompound("output"));

        consumption_progress = tag.getInt("consumption_progress");
        compression_progress = tag.getInt("compression_progress");

        target_stack = ItemStack.of(tag.getCompound("target"));
        //Calc compression target.
        if (!target_stack.isEmpty()) {
            compression_target = AvaritiaRecipeManager.getCompressorRecipeFromResult(target_stack).getCost();
        }
        fullContainerSync = true;
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        if (!input.isEmpty()) {
            CompoundNBT inputTag = new CompoundNBT();
            input.save(inputTag);
            tag.put("input", inputTag);
        }
        if (!output.isEmpty()) {
            CompoundNBT outputTag = new CompoundNBT();
            output.save(outputTag);
            tag.put("output", outputTag);
        }
        if (!target_stack.isEmpty()) {
            CompoundNBT targetTag = new CompoundNBT();
            target_stack.save(targetTag);
            tag.put("target", targetTag);
        }
        tag.putInt("consumption_progress", consumption_progress);
        tag.putInt("compression_progress", compression_progress);
        return super.save(tag);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side != null) {
                return inventoryHandlerLazyOptional[side.ordinal()].cast();
            } else {
                return inventoryHandlerLazyOptional[6].cast();
            }
        }
        return super.getCapability(capability, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < inventoryHandlerLazyOptional.length; x++)
            inventoryHandlerLazyOptional[x].invalidate();
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return input.isEmpty() && output.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == 0) {
            return input;
        } else {
            return output;
        }
    }

    @Override
    public ItemStack removeItem(int slot, int decrement) {
        if (slot == 0) {
            if (input.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                if (decrement < input.getCount()) {
                    ItemStack take = input.split(decrement);
                    if (input.getCount() <= 0) {
                        input = ItemStack.EMPTY;
                    }
                    return take;
                } else {
                    ItemStack take = input;
                    input = ItemStack.EMPTY;
                    return take;
                }
            }
        } else if (slot == 1) {
            if (output.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                if (decrement < output.getCount()) {
                    ItemStack take = output.split(decrement);
                    if (output.getCount() <= 0) {
                        output = ItemStack.EMPTY;
                    }
                    return take;
                } else {
                    ItemStack take = output;
                    output = ItemStack.EMPTY;
                    return take;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level.getBlockEntity(getBlockPos()) == this && player.distanceToSqr(Vector3d.atCenterOf(getBlockPos())) <= 64.0D;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot == 0) {
            if (target_stack.isEmpty()) {
                return true;
            }
            if (AvaritiaRecipeManager.hasCompressorRecipe(stack)) {
                if (AvaritiaRecipeManager.getCompressorRecipeFromInput(stack).getResult().sameItem(target_stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            input = stack;
        } else if (slot == 1) {
            output = stack;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return top;
        } else {
            return sides;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if (slot == 1 && side != Direction.UP) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    //@formatter:off
    @Override public int getMaxStackSize() { return 64; }
    @Override public ITextComponent getName() { return new TranslationTextComponent("container.avaritia.neutronium_compressor"); }
    @Override public boolean hasCustomName() { return false; }
    @Override public void startOpen(PlayerEntity player) { }
    @Override public void stopOpen(PlayerEntity player) { }
    @Override
    public void clearContent() {
        input = ItemStack.EMPTY;
        output = ItemStack.EMPTY;
    }

    @Override
    public ITextComponent getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new ContainerNeutroniumCompressor(id, playerInv, this);
    }
    //@formatter:on
}
