package morph.avaritia.util;

import morph.avaritia.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by covers1624 on 23/04/2017.
 */
public class ModHelper {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");


    public static final boolean isTinkersLoaded = ModList.get().isLoaded("tconstruct");
    private static Item tinkersCleaver;

    public static boolean isHoldingCleaver(LivingEntity entity) {
        if (!isTinkersLoaded) {
            return false;
        }
        if (tinkersCleaver == null) {
            tinkersCleaver = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "cleaver"));
        }
        return entity.isHolding(item -> Objects.equals(item, tinkersCleaver));
    }

    //TODO, move to ccl -covers
//    public static boolean isPlayerHolding(LivingEntity entity, Predicate<Item> predicate) {
//        for (Hand hand : Hand.values()) {
//            ItemStack stack = entity.getHeldItem(hand);
//            if (stack != null) {
//                if (predicate.test(stack.getItem())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public enum InfinityTier implements IItemTier {
        INFINITY_TIER();
        @Override
        public int getUses() {
            return 9999;
        }

        @Override
        public float getSpeed() {
            return 9999F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 6.0F;
        }

        @Override
        public int getLevel() {
            return 32;
        }

        @Override
        public int getEnchantmentValue() {
            return 200;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItems.infinity_ingot);
        }
    }
}
