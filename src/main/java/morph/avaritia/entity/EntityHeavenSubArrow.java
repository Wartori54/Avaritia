package morph.avaritia.entity;

import morph.avaritia.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class EntityHeavenSubArrow extends AbstractArrowEntity {

    public EntityHeavenSubArrow(EntityType entityType, World world) {
        super(entityType, world);
    }

    public EntityHeavenSubArrow(double x, double y, double z, World world) {
        super(ModEntities.heavenSubArrowEntity, x, y, z, world);
    }

    public EntityHeavenSubArrow(LivingEntity entity, World world) {
        super(ModEntities.heavenSubArrowEntity, entity, world);

    }

//    public EntityHeavenSubArrow(World world) {
//        super(world);
//    }

    @Override
    public void tick() {
        this.setRot(0, 0);
        super.tick();

        if (inGround && inGroundTime >= 20) {
            kill();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}
