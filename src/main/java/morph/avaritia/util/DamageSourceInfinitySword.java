package morph.avaritia.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DamageSourceInfinitySword extends EntityDamageSource {

    public DamageSourceInfinitySword(Entity source) {
        super("infinity", source);
    }

    @Override
    public ITextComponent getLocalizedDeathMessage(LivingEntity entity) {
        ItemStack itemstack = this.entity instanceof LivingEntity ? ((LivingEntity) this.entity).getMainHandItem() : null;
        String s = "death.attack.infinity";
        int rando = entity.level.random.nextInt(5);
        if (rando != 0) {
            s = s + "." + rando;
        }
        if (itemstack == null) {
            throw new IllegalArgumentException("killer entity has no item in hand H O W");
        } else {
            return new TranslationTextComponent(s, entity.getDisplayName(), itemstack.getDisplayName());
        }
    }

    @Override
    public boolean scalesWithDifficulty() {
        return false;
    }

}
