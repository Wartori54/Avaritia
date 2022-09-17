package morph.avaritia.item.tools;

import morph.avaritia.Avaritia;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ItemSwordSkulls extends SwordItem {

    public ItemSwordSkulls() {
        super(ItemTier.DIAMOND, 3, -2.4F, new Properties().stacksTo(1).tab(Avaritia.TAB).rarity(Rarity.EPIC).defaultDurability(9999));
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.skullfire_sword.desc").withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
    }
}
