package morph.avaritia.entity;

import codechicken.lib.vec.Vector3;
import morph.avaritia.init.ModEntities;
import morph.avaritia.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEndestPearl extends ProjectileItemEntity {


    public EntityEndestPearl(EntityType entityType, World world) {
        super(entityType, world);
    }

    public EntityEndestPearl(double x, double y, double z, World world) {
        super(ModEntities.endestPearlEntity, x, y, z, world);
    }

    public EntityEndestPearl(LivingEntity entity, World world) {
        super(ModEntities.endestPearlEntity, entity, world);
    }


    @Override
    protected void onHit(RayTraceResult pos) {
        super.onHit(pos);

        for (int i = 0; i < 100; ++i) {
            this.level.addParticle(ParticleTypes.PORTAL,
                    getX(),
                    getY(),
                    getZ(),
                    random.nextGaussian() * 3,
                    random.nextGaussian() * 3,
                    random.nextGaussian() * 3);
        }

        if (!this.level.isClientSide()) {
            //this.worldObj.createExplosion(this, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, 4.0f, true);

            Entity ent = new EntityGapingVoid(this.level);
            if (pos instanceof BlockRayTraceResult) {
                Direction dir = ((BlockRayTraceResult) pos).getDirection();
                Vector3 offset = new Vector3(dir.getStepX(), dir.getStepY(), dir.getStepZ());
                ent.setPos(getX() + offset.x * 0.25, getY() + offset.y * 0.25, getZ() + offset.z * 0.25);
                ent.xRot = 0.0F;
                ent.yRot = this.yRot;
            }
            level.addFreshEntity(ent);

            kill();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult pos) {
        super.onHitEntity(pos);
        pos.getEntity().hurt(DamageSource.thrown(this, getOwner()), 0.0F);

    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.endest_pearl;
    }

    @Override
    protected void defineSynchedData() {

    }
}
