package morph.avaritia.container;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by brandon3055 on 18/02/2017.
 */
public class SlotExtremeCrafting extends Slot {

    private final CraftingInventory craftMatrix;
    private final PlayerEntity player;
    private int amountCrafted;

    public SlotExtremeCrafting(PlayerEntity player, CraftingInventory craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        craftMatrix = craftingInventory;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (!getItem().isEmpty()) {
            amountCrafted += Math.min(amount, craftMatrix.getItem(getSlotIndex()).getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        amountCrafted += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        if (amountCrafted > 0) {
            stack.onCraftedBy(player.level, player, amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, stack, this.craftMatrix);
        }

        amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(PlayerEntity playerIn, ItemStack stack) {
        this.checkTakeAchievements(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(playerIn);
        NonNullList<ItemStack> slots = AvaritiaRecipeManager.getRemainingItems(craftMatrix, playerIn.level);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < slots.size(); ++i) {
            ItemStack itemstack = craftMatrix.getItem(i);
            ItemStack itemstack1 = slots.get(i);

            if (!itemstack.isEmpty()) {
                craftMatrix.removeItem(i, 1);
                itemstack = craftMatrix.getItem(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    craftMatrix.setItem(i, itemstack1);
                } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    craftMatrix.setItem(i, itemstack1);
                } else if (!player.inventory.add(itemstack1)) {
                    player.drop(itemstack1, false);
                }
            }
        }
        return stack;
    }
}
