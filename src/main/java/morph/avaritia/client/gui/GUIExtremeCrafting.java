package morph.avaritia.client.gui;

import codechicken.lib.texture.TextureUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import morph.avaritia.container.ContainerExtremeCrafting;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GUIExtremeCrafting extends ContainerScreen<ContainerExtremeCrafting> {

    private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/dire_crafting_gui.png");

    public GUIExtremeCrafting(ContainerExtremeCrafting container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
        this.imageHeight = 256;
        this.imageWidth = 238;
    }

//    @Override
//    protected void drawGuiContainerForegroundLayer(int i, int j) {
        //this.fontRendererObj.drawString(StatCollector.translateToLocal("container.extreme_crafting"), 28, 6, 4210752);
        //this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
//    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack stack, float par1, int par2, int par3) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        TextureUtils.changeTexture(GUI_TEX);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(stack, x, y, 0, 0, imageWidth, imageHeight);//TODO, this was, (x, y, 0, 0, ySize, ySize), Why was it ySize and not xSize..
    }
}
