package morph.avaritia.item;

import morph.avaritia.Avaritia;
import morph.avaritia.api.IHaloRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.entity.EntityEndestPearl;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemEndestPearl extends EnderPearlItem implements IHaloRenderItem, IModelRegister {

    public ItemEndestPearl() {
        super(new Properties().tab(Avaritia.TAB).rarity(Rarity.RARE));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCreative()) {
            stack.shrink(1);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!world.isClientSide()) {
            EntityEndestPearl pearl = new EntityEndestPearl(player, world);
            pearl.shoot(player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(pearl);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDrawHalo(ItemStack stack) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getHaloTexture(ItemStack stack) {
        return AvaritiaTextures.HALO;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getHaloSize(ItemStack stack) {
        return 4;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDrawPulse(ItemStack stack) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getHaloColour(ItemStack stack) {
        return 0xFF000000;
    }

    @Override // TODO: FIX THIS
    @OnlyIn(Dist.CLIENT)
    public void registerModels() {
//        ModelResourceLocation pearl = new ModelResourceLocation("avaritia:resource", "type=endest_pearl");
//        ModelLoader.registerItemVariants(this, pearl);
//        IBakedModel wrapped = new HaloRenderItem(TransformUtils.DEFAULT_ITEM, modelRegistry -> modelRegistry.getObject(pearl));
//        ModelRegistryHelper.register(pearl, wrapped);
//        ModelLoader.setCustomMeshDefinition(this, (ItemStack stack) -> pearl);
    }
}
