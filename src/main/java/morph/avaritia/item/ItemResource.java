package morph.avaritia.item;

import morph.avaritia.Avaritia;
import morph.avaritia.api.IHaloRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
/**
 * Created by covers1624 on 11/04/2017.
 */
public class ItemResource extends Item implements IHaloRenderItem, IModelRegister {
    private static final List<String> haloItems = Arrays.asList("neutron_pile",
                                                                "neutron_nugget",
                                                                "neutron_ingot",
                                                                "infinity_catalyst",
                                                                "infinity_ingot");

    public ItemResource(ResourceLocation name, Rarity rarity) {
        super(new Item.Properties().tab(Avaritia.TAB).rarity(rarity));
        this.setRegistryName(name);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDrawHalo(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        return (name != null && haloItems.contains(name.getPath()));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TextureAtlasSprite getHaloTexture(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name == null) {
            throw new IllegalStateException("Something is very wrong... stack turned null"); // we won't draw halo if it's null as stated on shouldDrawHalo
        }
        switch (name.getPath()) {
            case "neutron_pile":
            case "neutron_nugget":
            case "neutron_ingot":
                return AvaritiaTextures.HALO_NOISE;
            default:
                return AvaritiaTextures.HALO;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getHaloSize(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name == null) {
            throw new IllegalStateException("Something is very wrong... stack turned null"); // we won't draw halo if it's null as stated on shouldDrawHalo
        }
        switch (name.getPath()) {
            case "infinity_catalyst":
            case "infinity_ingot":
                return 10;
            default:
                return 8;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldDrawPulse(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name == null) {
            throw new IllegalStateException("Something is very wrong... stack turned null"); // we won't draw halo if it's null as stated on shouldDrawHalo
        }
        switch (name.getPath()) {
            case "infinity_catalyst":
            case "infinity_ingot":
                return true;
            default:
                return false;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getHaloColour(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name == null) {
            throw new IllegalStateException("Something is very wrong... stack turned null"); // we won't draw halo if it's null as stated on shouldDrawHalo
        }
        if (name.getPath().equals("neutron_pile")) {
            return 0x33FFFFFF;
        }
        if (name.getPath().equals("neutron_nugget")) {
            return 0x4DFFFFFF;
        }
        if (name.getPath().equals("neutron_ingot")) {
            return 0x99FFFFFF;
        }
        return 0xFF000000;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name == null) {
            throw new IllegalStateException("Something is very wrong... stack turned null"); // we won't draw halo if it's null as stated on shouldDrawHalo
        }
        switch (name.getPath()) {
            case "infinity_catalyst":
            case "infinity_ingot":
                return true;
            default:
                return false;
        }
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        ResourceLocation name = itemstack.getItem().getRegistryName();
        if (name == null) {
            throw new IllegalStateException("Something is very wrong... stack turned null"); // we won't draw halo if it's null as stated on shouldDrawHalo
        }
        switch (name.getPath()) {
            case "infinity_catalyst":
            case "infinity_ingot":
                return new EntityImmortalItem(world, location, itemstack);
            default:
                return null;
        }
    }

    @Override // TODO: fix this
    @OnlyIn(Dist.CLIENT)
    public void registerModels() {
//        super.registerModels();
//        Set<Integer> toRegister = Sets.newHashSet(2, 3, 4, 5, 6);
//
//        for (int meta : toRegister) {
//            String name = names.get(meta);
//            final ModelResourceLocation location = new ModelResourceLocation(getRegistryName(), "type=" + name);
//            IBakedModel wrapped = new HaloRenderItem(TransformUtils.DEFAULT_ITEM, modelRegistry -> modelRegistry.getObject(location));
//            ModelRegistryHelper.register(location, wrapped);
//        }
    }
}
