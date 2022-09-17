package morph.avaritia.entity;

import morph.avaritia.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class EntityHeavenArrow extends AbstractArrowEntity {

    public boolean impacted = false;
    public EntityHeavenArrow(EntityType entityType, World world) {
        super(entityType, world);
    }

    public EntityHeavenArrow(double x, double y, double z, World world) {
        super(ModEntities.heavenArrowEntity, x, y, z, world);
    }

    public EntityHeavenArrow(LivingEntity entity, World world) {
        super(ModEntities.heavenArrowEntity, entity, world);

    }

//    public EntityHeavenArrow(World world) {
//        super(world);
//    }

    @Override
    public void tick() {
        this.setRot(0, 0);
        super.tick();
        if (!impacted) {
            try {
                if (inGround) {
                    impacted = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (impacted) {
                if (!level.isClientSide()) {
                    barrage();
                }
            }
        }

        if (inGround && inGroundTime >= 100) {
            kill();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("impacted", impacted);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        impacted = tag.getBoolean("impacted");
    }

    public void barrage() {//TODO, this logic may be borked.
        Random randy = this.level.getRandom();
        for (int i = 0; i < 10; i++) {
            double angle = randy.nextDouble() * 2 * Math.PI;
            double dist = randy.nextGaussian() * 0.5;

            double x = Math.sin(angle) * dist + this.getX();
            double z = Math.cos(angle) * dist + this.getZ();
            double y = this.getY() + 25.0;

            double dangle = randy.nextDouble() * 2 * Math.PI;
            double ddist = randy.nextDouble() * 0.35;
            double dx = Math.sin(dangle) * ddist;
            double dz = Math.cos(dangle) * ddist;

            EntityHeavenSubArrow arrow = new EntityHeavenSubArrow(x, y, z, this.level);
            arrow.setOwner(getOwner());
            Vector3d movement = arrow.getDeltaMovement();
            arrow.setDeltaMovement(movement.add(dx, -(randy.nextDouble() * 1.85 + 0.15), dz));
            arrow.setBaseDamage(getBaseDamage());
            arrow.setCritArrow(true);
            arrow.pickup = this.pickup;

            level.addFreshEntity(arrow);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW); //TODO This needs to be null but can't be, Because vanulla.
    }
}
