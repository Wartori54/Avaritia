package morph.avaritia.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityImmortalItem extends ItemEntity {

    public EntityImmortalItem(World world, Entity original, ItemStack stack) {
        this(world, original.getX(), original.getY(), original.getZ(), stack);
        setPickUpDelay(20);
        setDeltaMovement(original.getDeltaMovement());
        setItem(stack);
    }

    public EntityImmortalItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z);
        setItem(stack);
    }

//    public EntityImmortalItem(World world, double x, double y, double z) {
//        super(world, x, y, z);
//        isImmuneToFire = true;
//    }
//
//    public EntityImmortalItem(World world) {
//        super(world);
//        isImmuneToFire = true;
//    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        return source == DamageSource.OUT_OF_WORLD;
    }

    // TODO: check if is persistent with unloads
}
