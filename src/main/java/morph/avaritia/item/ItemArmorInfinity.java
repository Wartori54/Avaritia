package morph.avaritia.item;

import codechicken.lib.item.SimpleArmorMaterial;
import morph.avaritia.Avaritia;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import morph.avaritia.client.render.entity.OldModelArmorInfinity;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.TextUtils;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemArmorInfinity extends ArmorItem {

    public static final SimpleArmorMaterial INFINITE_ARMOR = SimpleArmorMaterial.builder()
            .durability(new int[]{9999, 9999, 9999, 9999})
            .damageReduction(new int[]{6, 16, 12, 6})
            .enchantability(1000)
            .soundEvent(SoundEvents.ARMOR_EQUIP_IRON)
            .toughness(1.0F)
            .knockbackResistance(1.0F)
            .textureName("infinity")
            .repairMaterial(() -> Ingredient.EMPTY)
            .build(); //EnumHelper.addArmorMaterial("avaritia_infinity", "", 9999, new int[] { 6, 16, 12, 6 }, 1000, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);

    public ItemArmorInfinity(EquipmentSlotType slot) {
        super(INFINITE_ARMOR, slot, new Properties().tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
//        return "avaritia:textures/models/infinity_armor.png";
        return Avaritia.MOD_ID + ":textures/models/infinity_armor_layer_" + ((slot == EquipmentSlotType.HEAD || slot == EquipmentSlotType.CHEST || slot == EquipmentSlotType.FEET) ? "1" : "2") + ".png";
    }

    @Override
    public IArmorMaterial getMaterial() {
        return super.getMaterial();
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        ModelArmorInfinity model = (armorSlot == EquipmentSlotType.LEGS)
                ? ModelArmorInfinity.legModel : ModelArmorInfinity.armorModel;


//        model.update(entityLiving, itemStack, armorSlot);

        return (A) model;
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (slot == EquipmentSlotType.FEET) {
            tooltip.add(new StringTextComponent("\n+").append(
                        new StringTextComponent(TextUtils.makeSANIC(I18n.get("tip.sanic")))
                                .withStyle(TextFormatting.GRAY)
                                .withStyle(TextFormatting.ITALIC)
                        .append(new TranslationTextComponent("attribute.name.generic.movement_speed")
                                .withStyle(TextFormatting.RESET)
                                .withStyle(TextFormatting.GRAY))
                    )
            );
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack par1ItemStack) {
        return false;
    }

}
