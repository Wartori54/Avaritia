package morph.avaritia.handler;

import morph.avaritia.item.ItemArmorInfinity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Handles all abilities for ANY EntityLivingBase.
 * Some abilities are player specific, but just don't give a zombie your boots..
 */
public class AbilityHandler {

    //@formatter:off
    public static final Set<String> entitiesWithHelmets =     new HashSet<>();
    public static final Set<String> entitiesWithChestplates = new HashSet<>();
    public static final Set<String> entitiesWithLeggings =    new HashSet<>();
    public static final Set<String> entitiesWithBoots =       new HashSet<>();
    public static final Set<String> entitiesWithFlight =      new HashSet<>();
    //@formatter:on

    public static boolean isPlayerWearing(LivingEntity entity, EquipmentSlotType slot, Predicate<Item> predicate) {
        ItemStack stack = entity.getItemBySlot(slot);
        return !stack.isEmpty() && predicate.test(stack.getItem());
    }

    @SubscribeEvent
    //Updates all ability states for an entity, Handles firing updates and state changes.
    public void updateAbilities(LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) {
            return;
        }

        LivingEntity entity = event.getEntityLiving();
        String key = entity.getStringUUID() + "|" + entity.level.isClientSide();

        boolean hasHelmet = isPlayerWearing(event.getEntityLiving(), EquipmentSlotType.HEAD, item -> item instanceof ItemArmorInfinity);
        boolean hasChestplate = isPlayerWearing(event.getEntityLiving(), EquipmentSlotType.CHEST, item -> item instanceof ItemArmorInfinity);
        boolean hasLeggings = isPlayerWearing(event.getEntityLiving(), EquipmentSlotType.LEGS, item -> item instanceof ItemArmorInfinity);
        boolean hasBoots = isPlayerWearing(event.getEntityLiving(), EquipmentSlotType.FEET, item -> item instanceof ItemArmorInfinity);

        //Helmet toggle.
        if (hasHelmet) {
            entitiesWithHelmets.add(key);
            handleHelmetStateChange(entity, true);
        }
        if (!hasHelmet) {
            entitiesWithHelmets.remove(key);
            handleHelmetStateChange(entity, false);
        }

        //Chestplate toggle.
        if (hasChestplate) {
            entitiesWithChestplates.add(key);
            handleChestplateStateChange(entity, true);
        }
        if (!hasChestplate) {
            entitiesWithChestplates.remove(key);
            handleChestplateStateChange(entity, false);
        }

        //Leggings toggle.
        if (hasLeggings) {
            entitiesWithLeggings.add(key);
            handleLeggingsStateChange(entity, true);
        }
        if (!hasLeggings) {
            entitiesWithLeggings.remove(key);
            handleLeggingsStateChange(entity, false);
        }

        //Boots toggle.
        if (hasBoots) {
            handleBootsStateChange(entity);
            entitiesWithBoots.add(key);
        }
        if (!hasBoots) {
            handleBootsStateChange(entity);
            entitiesWithBoots.remove(key);
        }

        //Active ability ticking.
        if (entitiesWithHelmets.contains(key)) {
            tickHelmetAbilities(entity);
        }
        if (entitiesWithChestplates.contains(key)) {
            tickChestplateAbilities(entity);
        }
        if (entitiesWithLeggings.contains(key)) {
            tickLeggingsAbilities(entity);
        }
        if (entitiesWithBoots.contains(key)) {
            tickBootsAbilities(entity);
        }
    }

    /**
     * Strips all Abilities from an entity if the entity had any special abilities.
     *
     * @param entity EntityLivingBase we speak of.
     */
    private static void stripAbilities(LivingEntity entity) {
        String key = entity.getStringUUID() + "|" + entity.level.isClientSide();

        if (entitiesWithHelmets.remove(key)) {
            handleHelmetStateChange(entity, false);
        }

        if (entitiesWithChestplates.remove(key)) {
            handleChestplateStateChange(entity, false);
        }

        if (entitiesWithLeggings.remove(key)) {
            handleLeggingsStateChange(entity, false);
        }

        if (entitiesWithBoots.remove(key)) {
            handleBootsStateChange(entity);
        }
    }

    //region StateChanging
    private static void handleHelmetStateChange(LivingEntity entity, boolean isNew) {
        //TODO, Helmet abilities? Water breathing, NightVision, Auto Eat or No Hunger, No bad effects.
    }

    private static void handleChestplateStateChange(LivingEntity entity, boolean isNew) {
        String key = entity.getStringUUID() + "|" + entity.level.isClientSide();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) entity);
            if (isNew) {
                player.abilities.mayfly = true;
                entitiesWithFlight.add(key);
            } else {
                if (!player.isCreative() && entitiesWithFlight.contains(key)) {
                    player.abilities.mayfly = false;
                    player.abilities.flying = false;
                    entitiesWithFlight.remove(key);
                }
            }
        }
    }

    private static void handleLeggingsStateChange(LivingEntity entity, boolean isNew) {

    }

    private static void handleBootsStateChange(LivingEntity entity) {
        String temp_key = entity.getStringUUID() + "|" + entity.level.isClientSide();
        boolean hasBoots = isPlayerWearing(entity, EquipmentSlotType.FEET, item -> item instanceof ItemArmorInfinity);
        if (hasBoots) {
            entity.maxUpStep = 1.0625F;//Step 17 pixels, Allows for stepping directly from a path to the top of a block next to the path.
            entitiesWithBoots.add(temp_key);
        } else {
            if (entitiesWithBoots.contains(temp_key)) {
                entity.maxUpStep = 0.5F;
                entitiesWithBoots.remove(temp_key);
            }
        }
    }
    //endregion

    //region Ability Ticking
    private static void tickHelmetAbilities(LivingEntity entity) {

    }

    private static void tickChestplateAbilities(LivingEntity entity) {

    }

    private static void tickLeggingsAbilities(LivingEntity entity) {

    }

    private static void tickBootsAbilities(LivingEntity entity) {
        boolean flying = entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.flying;
        boolean swimming = entity.isSwimming();
        boolean sneaking = entity.isShiftKeyDown();

        float speed = (entity.isOnGround() ? 0.15f : 0.05f)
                * (flying ? 1.1f : 1.0f)
                * (swimming ? 1.2f : 1.0f)
                * (sneaking ? 0.1f : 1.0f);

        if (entity.zza > 0f) {
            entity.moveRelative(speed, new Vector3d(0f, 0f, 1f));
        } else if (entity.zza < 0f) {
            entity.moveRelative(-speed * 0.3f, new Vector3d(0f, 0f, 1f));
        }

        if (entity.xxa != 0f) {
            entity.moveRelative(speed * 0.5f * Math.signum(entity.xxa), new Vector3d(1f, 0f, 0f));
        }

        if (entity.isShiftKeyDown()) { // vanilla step up when shifting
            entity.maxUpStep = 0.5f;
        } else {
            entity.maxUpStep = 1.0625F;
        }
    }
    //endregion

    //region Ability Specific Events
    @SubscribeEvent
    public void jumpBoost(LivingJumpEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entitiesWithBoots.contains(entity.getStringUUID() + "|" + entity.level.isClientSide())) {
            Vector3d motion = entity.getDeltaMovement();
            entity.setDeltaMovement(motion.add(motion.x, motion.y + 0.45f, motion.z));
        }
    }
    //endregion

    //region Ability Striping Events
    //These are anything that should strip all abilities from an entity, Anything that creates an entity.
    @SubscribeEvent
    public void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        stripAbilities(event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        stripAbilities(event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        stripAbilities(event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        stripAbilities(event.getPlayer());
    }

    @SubscribeEvent
    public void entityConstructedEvent(EntityConstructing event) {
        if (event.getEntity() instanceof LivingEntity) {
            //stripAbilities((EntityLivingBase) event.getEntity());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        stripAbilities(event.getEntityLiving());
    }
    //endregion
}
