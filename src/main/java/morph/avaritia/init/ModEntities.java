package morph.avaritia.init;

import morph.avaritia.Avaritia;
import morph.avaritia.entity.EntityEndestPearl;
import morph.avaritia.entity.EntityGapingVoid;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.entity.EntityHeavenSubArrow;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Avaritia.MOD_ID)
public class ModEntities {
    @ObjectHolder("endest_pearl")
    public static EntityType<EntityEndestPearl> endestPearlEntity;
    @ObjectHolder("heaven_arrow")
    public static EntityType<EntityHeavenArrow> heavenArrowEntity;
    @ObjectHolder("heaven_sub_arrow")
    public static EntityType<EntityHeavenArrow> heavenSubArrowEntity;
    @ObjectHolder("gaping_void")
    public static EntityType<EntityGapingVoid> gapingVoidEntity;


    public static void init(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(EntityType.Builder.of(EntityEndestPearl::new, EntityClassification.MISC)
                        .setTrackingRange(64)
                        .updateInterval(10)
                        .setShouldReceiveVelocityUpdates(true)
                        .sized(0.25F, 0.25F)
                .build("endest_pearl").setRegistryName("endest_pearl"));
        event.getRegistry().register(EntityType.Builder.of(EntityHeavenArrow::new, EntityClassification.MISC)
                        .setTrackingRange(32)
                        .updateInterval(1)
                        .setShouldReceiveVelocityUpdates(true)
                        .sized(0.5F, 0.5F)
                .build("heaven_arrow").setRegistryName("heaven_arrow"));
        event.getRegistry().register(EntityType.Builder.of(EntityHeavenSubArrow::new, EntityClassification.MISC)
                        .setTrackingRange(32)
                        .updateInterval(1)
                        .setShouldReceiveVelocityUpdates(true)
                        .sized(0.5F, 0.5F)
                .build("heaven_sub_arrow").setRegistryName("heaven_sub_arrow"));
        event.getRegistry().register(EntityType.Builder.of(EntityGapingVoid::new, EntityClassification.MISC)
                    .setTrackingRange(256)
                    .updateInterval(10)
                    .setShouldReceiveVelocityUpdates(false)
                    .fireImmune()
                    .sized(0.1F, 0.1F)
                .build("gaping_void").setRegistryName("gaping_void")
        );

    }
}
