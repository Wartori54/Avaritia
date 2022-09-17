package morph.avaritia.client.gui;

import codechicken.lib.math.MathHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import morph.avaritia.container.ContainerNeutronCollector;
import morph.avaritia.tile.TileNeutronCollector;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GUINeutronCollector extends GuiMachineBase<ContainerNeutronCollector> {

    private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/neutron_collector_gui.png");

    private TileNeutronCollector collector = null;

    public GUINeutronCollector(ContainerNeutronCollector container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
        setBackgroundTexture(GUI_TEX);
    }

    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
        super.renderLabels(stack, mouseX, mouseY);
//        ITextComponent s = new TranslationTextComponent("container.neutron_collector");
        float scaled_progress = scaleF(this.menu.getProgress(), this.menu.getTotalProgress(), 100);
        ITextComponent progress = new StringTextComponent("Progress: " + MathHelper.round(scaled_progress, 10) + "%"); // TODO: make this TranslationTextComponent
//        this.font.draw(stack, s, width / 2F - font.width(s) / 2F, 6, 0x404040);
        this.font.draw(stack, progress, this.imageWidth / 2F - font.width(progress) / 2F, this.imageHeight - 104, 0x404040);
//        this.font.draw(stack, new TranslationTextComponent("container.inventory"), 8, height - 96 + 2, 0x404040);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawBackground(stack);
        super.renderBg(stack, partialTicks, mouseX, mouseY);
    }
}
