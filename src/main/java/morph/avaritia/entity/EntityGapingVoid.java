package morph.avaritia.entity;

import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import morph.avaritia.init.ModEntities;
import morph.avaritia.init.ModSounds;
import morph.avaritia.proxy.Proxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class EntityGapingVoid extends Entity {

    public static final DataParameter<Integer> AGE_PARAMETER = EntityDataManager.defineId(EntityGapingVoid.class, DataSerializers.INT);

    private static Random randy = new Random();
    public static final int maxLifetime = 186;
    public static double collapse = .95;
    public static double suckRange = 20.0;

    public static final Predicate<Entity> SUCK_PREDICATE = input -> {
        if (input instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) input;
            if (p.isCreative() && p.abilities.flying) {
                return false;
            }
        }

        return true;
    };

    public static final Predicate<Entity> OMNOM_PREDICATE = input -> {
        if (!(input instanceof LivingEntity)) {
            return false;
        }

        if (input instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) input;
            if (p.isCreative()) {
                return false;
            }
        }

        return true;
    };

    private FakePlayer fakePlayer;

    public EntityGapingVoid(EntityType entityType, World world) {
        super(entityType, world);
    }

    public EntityGapingVoid(World world) {
        super(ModEntities.gapingVoidEntity, world);
        noCulling = true;
        if (world instanceof ServerWorld) {
            fakePlayer = FakePlayerFactory.get((ServerWorld) world, Proxy.avaritiaFakePlayer);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(AGE_PARAMETER, 0);
    }

    @Override
    public void tick() {
        super.tick();

        // tick, tock
        int age = getAge();

        if (age >= maxLifetime) {
            this.level.explode(this, getX(), getY(), getZ(), 6.0f, Explosion.Mode.DESTROY);
            kill();
        } else {
            if (age == 0) {
                this.level.playSound(null, getX(), getY(), getZ(), ModSounds.GAPING_VOID, SoundCategory.HOSTILE, 8.0F, 1.0F);
            }
            setAge(age + 1);
        }

        if (this.level.isClientSide()) {
            //we dont want to do any of this on the client.
            return;
        }
        if (fakePlayer == null) {
            //wot.
            kill();
            return;
        }
        Vector3 pos = Vector3.fromEntity(this);

        // poot poot
        double particlespeed = 4.5;

        double size = getVoidScale(age) * 0.5 - 0.2;
        for (int i = 0; i < 50; i++) {
            Vector3 particlePos = new Vector3(0, 0, size);
            particlePos.rotate(randy.nextFloat() * 180f, new Vector3(0, 1, 0));
            particlePos.rotate(randy.nextFloat() * 360f, new Vector3(1, 0, 0));

            Vector3 velocity = particlePos.copy().normalize();
            velocity.multiply(particlespeed);
            particlePos.add(pos);

            this.level.addParticle(ParticleTypes.PORTAL, particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }

        // *slurping noises*
        Cuboid6 cuboid = new Cuboid6().add(pos);
        cuboid.expand(suckRange);
        List<Entity> sucked = this.level.getEntitiesOfClass(Entity.class, cuboid.aabb(), SUCK_PREDICATE);

        double radius = getVoidScale(age) * 0.5;

        for (Entity suckee : sucked) {
            if (suckee != this) {
                double dx = getX() - suckee.getX();
                double dy = getY() - suckee.getY();
                double dz = getZ() - suckee.getZ();

                double lensquared = dx * dx + dy * dy + dz * dz;
                double len = Math.sqrt(lensquared);
                double lenn = len / suckRange;

                if (len <= suckRange) {
                    double strength = (1 - lenn) * (1 - lenn);
                    double power = 0.075 * radius;

                    Vector3d motion = suckee.getDeltaMovement()
                            .add(
                                    (dx / len) * strength * power,
                                    (dy / len) * strength * power,
                                    (dz / len) * strength * power
                            );
                    suckee.setDeltaMovement(motion);
                }
            }
        }

        // om nom nom
        double nomrange = radius * 0.95;
        cuboid = new Cuboid6().add(pos);
        cuboid.expand(nomrange);
        List<Entity> nommed = this.level.getEntitiesOfClass(LivingEntity.class, cuboid.aabb(), OMNOM_PREDICATE);

        for (Entity nommee : nommed) {
            if (nommee != this) {
                Vector3 nomedPos = Vector3.fromEntity(nommee);
                Vector3 diff = pos.copy().subtract(nomedPos);

                double len = diff.mag();

                if (len <= nomrange) {
                    nommee.hurt(DamageSource.OUT_OF_WORLD, 3.0f);
                }
            }
        }

        // every half second, SMASH STUFF
        if (age % 10 == 0) {
            Vector3 posFloor = pos.copy().floor();

            int blockrange = (int) Math.round(nomrange);

            for (int y = -blockrange; y <= blockrange; y++) {
                for (int z = -blockrange; z <= blockrange; z++) {
                    for (int x = -blockrange; x <= blockrange; x++) {
                        Vector3 pos2 = new Vector3(x, y, z);
                        Vector3 rPos = posFloor.copy().add(pos2);
                        BlockPos blockPos = rPos.pos();

                        if (blockPos.getY() < 0 || blockPos.getY() > 255) {
                            continue;
                        }

                        double dist = pos2.mag();
                        if (dist <= nomrange && !this.level.isEmptyBlock(blockPos)) {
                            BlockState state = this.level.getBlockState(blockPos);
                            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(this.level, blockPos, state, fakePlayer);
                            MinecraftForge.EVENT_BUS.post(event);
                            if (!event.isCanceled()) {
                                float resist = state.getBlock().getExplosionResistance();//TODO HELP state.getExplosionResistance(this, this.worldObj, lx, ly, lz, this.posX, this.posY, this.posZ);
                                if (resist <= 10.0) {
                                    Block.dropResources(state, this.level, blockPos);
                                    this.level.removeBlock(blockPos, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setAge(int age) {
        this.entityData.set(AGE_PARAMETER, age);
    }

    public int getAge() {
        return this.entityData.get(AGE_PARAMETER);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT tag) {
        setAge(tag.getInt("age"));
        if (this.level instanceof ServerWorld) {
            fakePlayer = FakePlayerFactory.get((ServerWorld) this.level, Proxy.avaritiaFakePlayer);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT tag) {
        tag.putInt("age", getAge());
    }

    public static double getVoidScale(double age) {
        double life = age / (double) maxLifetime;

        double curve;
        if (life < collapse) {
            curve = 0.005 + ease(1 - ((collapse - life) / collapse)) * 0.995;
        } else {
            curve = ease(1 - ((life - collapse) / (1 - collapse)));
        }
        return 10.0 * curve;
    }

    private static double ease(double in) {
        double t = in - 1;
        return Math.sqrt(1 - t * t);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }
}
