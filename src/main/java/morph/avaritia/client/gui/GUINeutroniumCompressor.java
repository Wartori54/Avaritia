package morph.avaritia.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import morph.avaritia.container.ContainerNeutroniumCompressor;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.covers1624.lib.gui.DrawableGuiElement.AnimationDirection;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;

public class GUINeutroniumCompressor extends GuiMachineBase<ContainerNeutroniumCompressor> {

    private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/compressor.png");

    private TileNeutroniumCompressor compressor = null;

    public GUINeutroniumCompressor(ContainerNeutroniumCompressor container, PlayerInventory player, ITextComponent name) {
        super(container, player, name);
        setBackgroundTexture(GUI_TEX);

        if (true)
            return;
        DrawableBuilder builder = drawableBuilder();
        builder.setGuiLocation(62, 35).setSpriteSize(176, 0, 22, 16);
        builder.setAnimationDirection(AnimationDirection.LEFT_RIGHT);
        builder.setRenderPredicate(() -> compressor.getConsumptionProgress() > 0);
        builder.setAnimationSupplier(() -> Pair.of(kick(compressor.getConsumptionProgress()), compressor.getConsumptionTarget()));
        addDrawable(builder.build());

        builder = drawableBuilder();
        builder.setGuiLocation(90, 35).setSpriteSize(176, 16, 16, 16);
        builder.setAnimationDirection(AnimationDirection.BOTTOM_UP);
        builder.setRenderPredicate(() -> compressor.getCompressionProgress() > 0);
        builder.setAnimationSupplier(() -> Pair.of(kick(compressor.getCompressionProgress()), compressor.getCompressionTarget()));
        builder.setTooltipSupplier(() ->
                Collections.singletonList(
                        new StringTextComponent(
                                String.format("%.2f%%",
                                        scaleF(compressor.getCompressionProgress(),
                                                compressor.getCompressionTarget(),
                                                100)
                                )
                        )
                )
        );
        addDrawable(builder.build());
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
        super.renderLabels(stack, mouseX, mouseY);
        if (compressor == null) return;
        if (compressor.getCompressionProgress() > 0) {
            String progress = compressor.getCompressionProgress() + " / " + compressor.getCompressionTarget();
            int x = width / 2 - this.font.width(progress) / 2;
            this.font.draw(stack, progress, x, 60, 0x404040);
        }
        if (!compressor.getTargetStack().isEmpty()) {
            String text = "Output"; // TODO: yet another untranslatable text
            int x = (width + 147 - 8) / 2 - this.font.width(text) / 2;
            this.font.draw(stack, text, x, 25, 0x404040);
        }
        if (!compressor.getTargetStack().isEmpty()) {
            String text = "Input"; // two in a row
            if (compressor.getInputItems() != null && compressor.getInputItems().size() > 1) {
                text += "s";
            }
            int x = 20 - this.font.width(text) / 2;
            this.font.draw(stack, text, x, 25, 0x404040);
        }

    }

    private int kick(int num) {
        if (num > 0) {
            return num + 1;
        }
        return num;
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawBackground(stack);
        super.renderBg(stack, partialTicks, mouseX, mouseY);
    }
}
