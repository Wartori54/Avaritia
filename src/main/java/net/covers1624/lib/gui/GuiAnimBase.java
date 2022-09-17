package net.covers1624.lib.gui;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Rectangle4i;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import morph.avaritia.container.ContainerMachineBase;
import net.covers1624.lib.gui.DrawableGuiElement.AnimationDirection;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Created by covers1624 on 21/05/2017.
 */
public abstract class GuiAnimBase<T extends ContainerMachineBase> extends ContainerScreen<T> {

    private Set<DrawableGuiElement> drawableElements = new HashSet<>();
    private ResourceLocation BACKGROUND_TEX;

    public GuiAnimBase(T container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    protected void setBackgroundTexture(ResourceLocation location) {
        BACKGROUND_TEX = location;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        renderFg(stack, mouseX, mouseY, partialTicks);
    }

//    @Override // no override cuz super method is gone, just call after render
    protected void renderFg(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(-leftPos, -topPos, 0);
        for (DrawableGuiElement drawableGuiElement : drawableElements) {
            Point guiPos = getGuiPos();
            Rectangle4i bounds = drawableGuiElement.getBounds().offset(guiPos.x, guiPos.y);
            if (bounds.contains(mouseX, mouseY)) {
                drawableGuiElement.renderTooltip(stack, new Point(mouseX, mouseY), this.font);
                break;
            }
        }
        RenderSystem.popMatrix();
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        Point guiPos = getGuiPos();
        RenderSystem.pushMatrix();
        RenderSystem.translatef(guiPos.x, guiPos.y, 0);
        for (DrawableGuiElement drawableElement : drawableElements) {
            try {
                drawableElement.draw(stack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RenderSystem.popMatrix();
    }

    protected void addDrawable(DrawableGuiElement element) {
        drawableElements.add(element);
    }

    protected DrawableBuilder drawableBuilder() {
        return new DrawableBuilder().setParent(this).setSpriteLocation(BACKGROUND_TEX).setAnimationDirection(AnimationDirection.STATIC);
    }

    protected Point getGuiPos() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        return new Point(x, y);
    }

    protected void drawBackground(MatrixStack stack) {
        Point guiPos = getGuiPos();
        TextureUtils.changeTexture(BACKGROUND_TEX);
        this.blit(stack, guiPos.x, guiPos.y, 0, 0, imageWidth, imageHeight);
    }

    protected static class DrawableBuilder {

        private ContainerScreen<?> parent;
        private ResourceLocation spriteLocation;
        private Rectangle4i sprite;
        private Point location;
        private BooleanSupplier renderPredicate;
        private Supplier<Pair<Integer, Integer>> animationSupplier;
        private AnimationDirection animationDirection;
        private Supplier<List<ITextComponent>> tooltipSupplier;

        public DrawableBuilder() {
        }

        public DrawableBuilder setParent(ContainerScreen<?> parent) {
            this.parent = parent;
            return this;
        }

        public DrawableBuilder setSpriteLocation(ResourceLocation spriteLocation) {
            this.spriteLocation = spriteLocation;
            return this;
        }

        public DrawableBuilder setSpriteSize(Rectangle4i sprite) {
            this.sprite = sprite;
            return this;
        }

        public DrawableBuilder setGuiLocation(Point location) {
            this.location = location;
            return this;
        }

        public DrawableBuilder setSpriteSize(int x, int y, int w, int h) {
            return setSpriteSize(new Rectangle4i(x, y, w, h));
        }

        public DrawableBuilder setGuiLocation(int x, int y) {
            return setGuiLocation(new Point(x, y));
        }

        public DrawableBuilder setAnimationSupplier(Supplier<Pair<Integer, Integer>> animationSupplier) {
            this.animationSupplier = animationSupplier;
            return this;
        }

        public DrawableBuilder setAnimationDirection(AnimationDirection animationDirection) {
            this.animationDirection = animationDirection;
            return this;
        }

        public DrawableBuilder setRenderPredicate(BooleanSupplier renderPredicate) {
            this.renderPredicate = renderPredicate;
            return this;
        }

        public DrawableBuilder setTooltipSupplier(Supplier<List<ITextComponent>> tooltipSupplier) {
            this.tooltipSupplier = tooltipSupplier;
            return this;
        }

        public DrawableGuiElement build() {
            return new DrawableGuiElement(parent, spriteLocation, sprite, location, animationDirection, animationSupplier, renderPredicate, tooltipSupplier);
        }
    }
}
