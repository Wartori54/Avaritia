package morph.avaritia.tile;

import morph.avaritia.container.ContainerExtremeCrafting;
import morph.avaritia.init.ModContent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TileDireCraftingTable extends TileBase implements IInventory, ISidedInventory, INamedContainerProvider, INameable {

    private ItemStack result = ItemStack.EMPTY;
    private final NonNullList<ItemStack> matrix = NonNullList.withSize(81, ItemStack.EMPTY);
    public static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("tile.avaritia.extreme_crafting_table");

    public TileDireCraftingTable() {
        super(ModContent.tileDireCraftingTable);
        while (matrix.size() < 81) matrix.add(ItemStack.EMPTY);
    }


    @Override
    public void load(BlockState blockState, CompoundNBT tag) {
        super.load(blockState, tag);
        result = ItemStack.of(tag.getCompound("Result"));
        for (int x = 0; x < matrix.size(); x++) {
            if (tag.contains("Craft" + x)) {
                matrix.set(x, ItemStack.of(tag.getCompound("Craft" + x)));
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        if (!result.isEmpty()) {
            CompoundNBT produce = new CompoundNBT();
            result.save(produce);
            tag.put("Result", produce);
        } else {
            tag.remove("Result");
        }

        for (int x = 0; x < matrix.size(); x++) {
            if (!matrix.get(x).isEmpty()) {
                CompoundNBT craft = new CompoundNBT();
                matrix.get(x).save(craft);
                tag.put("Craft" + x, craft);
            } else {
                tag.remove("Craft" + x);
            }
        }
        return super.save(tag);
    }

    @Override
    public int getContainerSize() {
        return 82;
    }

    @Override
    public boolean isEmpty() {
        return this.matrix.stream().allMatch(ItemStack::isEmpty) && this.result.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == 0) {
            return result;
        } else if (slot <= matrix.size()) {
            return matrix.get(slot - 1);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItem(int slot, int decrement) {
        if (slot == 0) {
            if (!result.isEmpty()) {
                for (int x = 1; x <= matrix.size(); x++) {
                    removeItem(x, 1);
                }
                if (result.getCount() <= decrement) {
                    ItemStack craft = result;
                    result = ItemStack.EMPTY;
                    return craft;
                }
                ItemStack split = result.split(decrement);
                if (result.getCount() <= 0) {
                    result = ItemStack.EMPTY;
                }
                return split;
            } else {
                return ItemStack.EMPTY;
            }
        } else if (slot <= matrix.size()) {
            if (matrix.get(slot - 1) != ItemStack.EMPTY) {
                if (matrix.get(slot - 1).getCount() <= decrement) {
                    ItemStack ingredient = matrix.get(slot - 1);
                    matrix.set(slot - 1, ItemStack.EMPTY);
                    return ingredient;
                }
                ItemStack split = matrix.get(slot - 1).split(decrement);
                if (matrix.get(slot - 1).getCount() <= 0) {
                    matrix.set(slot - 1, ItemStack.EMPTY);
                }
                return split;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == 0) {
            if (!result.isEmpty()) {
                for (int x = 1; x <= matrix.size(); x++) {
                    removeItem(x, 1);
                }

                ItemStack craft = result;
                result = ItemStack.EMPTY;
                return craft;

            } else {
                return ItemStack.EMPTY;
            }
        } else if (slot <= matrix.size()) {
            if (!matrix.get(slot - 1).isEmpty()) {
                ItemStack ingredient = matrix.get(slot - 1);
                matrix.set(slot - 1, ItemStack.EMPTY);
                return ingredient;
            }
        }
        return ItemStack.EMPTY;
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

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            result = stack;
        } else if (slot <= matrix.size()) {
            matrix.set(slot - 1, stack);
        }
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("container.dire");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return getName();
    }

    @Override
    public int[] getSlotsForFace(Direction face) {
        return new int[] {};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack item, Direction face) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack item, Direction face) {
        return false;
    }

    @Override
    public void clearContent() {
        result = ItemStack.EMPTY;
        matrix.clear();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new ContainerExtremeCrafting(id, playerInv, this);
    }
}
