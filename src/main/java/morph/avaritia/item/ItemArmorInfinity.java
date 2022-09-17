package morph.avaritia.item;

import codechicken.lib.item.SimpleArmorMaterial;
import morph.avaritia.Avaritia;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.TextUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ItemArmorInfinity extends ArmorItem {

    public static final SimpleArmorMaterial INFINITE_ARMOR = SimpleArmorMaterial.builder()
            .durability(new int[]{9999, 9999, 9999, 9999})
            .damageReduction(new int[]{6, 16, 12, 6})
            .enchantability(1000)
            .soundEvent(SoundEvents.ARMOR_EQUIP_IRON)
            .toughness(1.0F)
            .knockbackResistance(1.0F).build(); //EnumHelper.addArmorMaterial("avaritia_infinity", "", 9999, new int[] { 6, 16, 12, 6 }, 1000, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);

    public ItemArmorInfinity(EquipmentSlotType slot) {
        super(INFINITE_ARMOR, slot, new Properties().tab(Avaritia.TAB).rarity(ModItems.COSMIC_RARITY));
    }

//    @Override
//    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
//        return "avaritia:textures/models/infinity_armor.png";
//    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT) // TODO: more rendering fixing wahoooo!!!!
//    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot, ModelBiped _deafult) {
//        ModelArmorInfinity model = armorSlot == EntityEquipmentSlot.LEGS ? ModelArmorInfinity.legModel : ModelArmorInfinity.armorModel;
//
//        model.update(entityLiving, itemstack, armorSlot);
//
//        return model;
//    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (slot == EquipmentSlotType.FEET) {
            tooltip.add(new StringTextComponent("+").append(
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
