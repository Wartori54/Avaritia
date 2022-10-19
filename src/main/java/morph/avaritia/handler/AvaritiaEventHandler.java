package morph.avaritia.handler;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.ItemArmorInfinity;
import morph.avaritia.item.ItemMatterCluster;
import morph.avaritia.item.tools.ItemSwordInfinity;
import morph.avaritia.util.DamageSourceInfinitySword;
import morph.avaritia.util.ModHelper;
import morph.avaritia.util.TextUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class AvaritiaEventHandler {

    private static final Map<RegistryKey<World>, List<AEOCrawlerTask>> crawlerTasks = new HashMap<>();
    //These are defaults, loaded from config.
    public static final Set<ResourceLocation> defaultTrashOres = new HashSet<>();

    public static boolean isInfinite(PlayerEntity player) {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType() != EquipmentSlotType.Group.ARMOR) {
                continue;
            }
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmorInfinity)) {
                return false;
            }
        }
        return true;
    }

    public static AEOCrawlerTask startCrawlerTask(World world, PlayerEntity player, ItemStack stack, BlockPos coords, int steps, boolean leaves, boolean force, Set<BlockPos> posChecked) {
        AEOCrawlerTask swapper = new AEOCrawlerTask(world, player, stack, coords, steps, leaves, force, posChecked);
        RegistryKey<World> dim = world.dimension();
        if (!crawlerTasks.containsKey(dim)) {
            crawlerTasks.put(dim, new ArrayList<>());
        }
        crawlerTasks.get(dim).add(swapper);
        return swapper;
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent.WorldTickEvent event) {//TODO, clamp at specific num ops per tick.
        if (event.phase == TickEvent.Phase.END) {
            RegistryKey<World> dim = event.world.dimension();
            if (crawlerTasks.containsKey(dim)) {
                List<AEOCrawlerTask> swappers = crawlerTasks.get(dim);
                List<AEOCrawlerTask> swappersSafe = new ArrayList<>(swappers);
                swappers.clear();
                for (AEOCrawlerTask s : swappersSafe) {
                    if (s != null) {
                        s.tick();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (!(armorStack.getItem() instanceof ArmorItem)) {
                continue;
            }
            EquipmentSlotType armorType = ((ArmorItem) armorStack.getItem()).getSlot();
            if (armorType == EquipmentSlotType.HEAD) {
                player.setAirSupply(300);
                player.getFoodData().setFoodLevel(20);
                player.getFoodData().setSaturation(20F);
                EffectInstance nv = player.getEffect(Effects.NIGHT_VISION);
                if (nv == null) {
                    nv = new EffectInstance(Effects.NIGHT_VISION, 300, 0, false, false);
                    player.addEffect(nv);
                }
                nv.duration = 300;
            } else if (armorType == EquipmentSlotType.CHEST) {
                player.abilities.mayfly = true;
                List<EffectInstance> effects = Lists.newArrayList(player.getActiveEffects());
                for (EffectInstance potion : Collections2.filter(effects, potion -> !potion.getEffect().isBeneficial())) {
                    if (ModHelper.isHoldingCleaver(player) && potion.getEffect().equals(Effects.DIG_SLOWDOWN)) {
                        continue;
                    }
                    player.removeEffect(potion.getEffect());
                }
            } else if (armorType == EquipmentSlotType.LEGS) {
                if (player.isOnFire()) {
                    player.clearFire();
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerMine(PlayerInteractEvent.LeftClickBlock event) {
        if (!ConfigHandler.bedrockBreaker || event.getFace() == null || event.getWorld().isClientSide() || event.getItemStack().isEmpty() || event.getPlayer().isCreative()) {
            return;
        }
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (state.getDestroySpeed(world, event.getPos()) <= -1 && event.getItemStack().getItem() == ModItems.infinity_pickaxe && (state.getMaterial() == Material.STONE || state.getMaterial() == Material.METAL)) {

            if (event.getItemStack().getTag() != null && event.getItemStack().getTag().getBoolean("hammer")) {
                ModItems.infinity_pickaxe.onBlockStartBreak(event.getPlayer().getMainHandItem(), event.getPos(), event.getPlayer());
            } else {//TODO, FIXME, HELP!
                //if (block.quantityDropped(randy) == 0) {
                //    ItemStack drop = block.getPickBlock(state, ToolHelper.raytraceFromEntity(event.getWorld(), event.getPlayerEntity(), true, 10), event.getWorld(), event.getPos(), event.getPlayerEntity());
                //    if (drop == null) {
                //        drop = new ItemStack(block, 1, meta);
                //    }
                //    ToolHelper.dropItem(drop, event.getPlayerEntity().worldObj, event.getPos());
                //} else {
                //    block.harvestBlock(event.getWorld(), event.getPlayerEntity(), event.getPos(), state, null, null);
                ///}
                //event.getWorld().setBlockToAir(event.getPos());
                //event.world.playAuxSFX(2001, event.getPos(), Block.getIdFromBlock(block) + (meta << 12));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemSwordInfinity) {
            for (int x = 0; x < event.getToolTip().size(); x++) {
                ITextComponent text = event.getToolTip().get(x);
                if (text instanceof StringTextComponent && text.getContents().equals(" ")) {
                    if (text.getSiblings().size() == 1) {
                        ITextComponent innerText = text.getSiblings().get(0);
                        if (innerText instanceof TranslationTextComponent) {
                            TranslationTextComponent attribute_modifier = (TranslationTextComponent) innerText;
                            if (attribute_modifier.getArgs().length == 2 && attribute_modifier.getKey().equals("attribute.modifier.equals.0")) {
                                if (attribute_modifier.getArgs()[1] instanceof TranslationTextComponent) {
                                    TranslationTextComponent attack_damage_text = (TranslationTextComponent) attribute_modifier.getArgs()[1];
                                    if (attack_damage_text.getKey().equals("attribute.name.generic.attack_damage")) {
                                        // FINALLY, we're on correct tag
                                        attribute_modifier.args[0] = TextUtils.makeFabulous(I18n.get("tip.infinity"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGetHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() == ModItems.infinity_sword && player.isUsingItem()) {//TODO Blocking? Maybe add a shield?
            event.setCanceled(true);
        }
        if (isInfinite(player) && !(event.getSource() instanceof DamageSourceInfinitySword)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttacked(LivingAttackEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }
        if (event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof PlayerEntity) {
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (isInfinite(player) && !(event.getSource() instanceof DamageSourceInfinitySword)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.isRecentlyHit() && event.getEntityLiving() instanceof AbstractSkeletonEntity && event.getSource().getDirectEntity() instanceof PlayerEntity) { // TODO: this logic is very BORKED
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();
            if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() == ModItems.skull_sword) {
                // ok, we need to drop a skull then.
                if (event.getDrops().isEmpty()) {
                    addDrop(event, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
                } else {
                    int skulls = 0;
                    for (ItemEntity drop : event.getDrops()) {
                        ItemStack stack = drop.getItem();
                        if (stack.getItem() instanceof SkullItem) {
                            if (stack.getCount() > 0) {
                                skulls++;
                            } else if (stack.getCount() == 0) {
                                skulls++;
                                stack.setCount(1);
                            }
                        }
                    }

                    if (skulls == 0) {
                        addDrop(event, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void diggity(BreakSpeed event) {
        if (!event.getEntityLiving().getMainHandItem().isEmpty()) {
            ItemStack held = event.getEntityLiving().getMainHandItem();
            if (held.getItem() == ModItems.infinity_pickaxe || held.getItem() == ModItems.infinity_shovel) {
                if (!event.getEntityLiving().isOnGround()) {
                    event.setNewSpeed(event.getNewSpeed() * 5);
                }
                if (!event.getEntityLiving().isInWater() && !EnchantmentHelper.hasAquaAffinity(event.getEntityLiving())) {
                    event.setNewSpeed(event.getNewSpeed() * 5);
                }
                if (held.getTag() != null) {
                    if (held.getTag().getBoolean("hammer") || held.getTag().getBoolean("destroyer")) {
                        event.setNewSpeed(event.getNewSpeed() * 0.5F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void canHarvest(PlayerEvent.HarvestCheck event) {
        if (!event.getEntityLiving().getMainHandItem().isEmpty()) {
            ItemStack held = event.getEntityLiving().getMainHandItem();
            if (held.getItem() == ModItems.infinity_shovel && event.getTargetBlock().getMaterial() == Material.STONE) {
                if (held.getTag() != null && held.getTag().getBoolean("destroyer") && isGarbageBlock(event.getTargetBlock().getBlock())) {
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }

    private static boolean isGarbageBlock(Block block) {
        return block == Blocks.COBBLESTONE
                || block == Blocks.STONE
                || block == Blocks.NETHERRACK;

    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (isInfinite(player) && !(event.getSource() instanceof DamageSourceInfinitySword)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    private void addDrop(LivingDropsEvent event, ItemStack drop) {
        ItemEntity entityitem = new ItemEntity(event.getEntityLiving().level, event.getEntityLiving().getX(), event.getEntityLiving().getY(), event.getEntityLiving().getZ(), drop);
        entityitem.setDefaultPickUpDelay();
        event.getDrops().add(entityitem);
    }

    @SubscribeEvent
    public void clusterClustererererer(EntityItemPickupEvent event) {
        if (event.getPlayer() != null && event.getItem().getItem().getItem() == ModItems.matter_cluster) {
            ItemStack stack = event.getItem().getItem();
            if (stack.isEmpty()) return;
            PlayerEntity player = event.getPlayer();

            for (ItemStack slot : player.inventory.items) {
                if (slot.getItem() == ModItems.matter_cluster) {
                    ItemMatterCluster.mergeClusters(stack, slot);
                }
            }
        }
    }

}
