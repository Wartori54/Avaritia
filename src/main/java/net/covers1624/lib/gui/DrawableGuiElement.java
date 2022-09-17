package net.covers1624.lib.gui;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Rectangle4i;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Point;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Created by covers1624 on 21/05/2017.
 */
public class DrawableGuiElement {

    private final ContainerScreen<?> parent;
    private final ResourceLocation SPRITE_LOCATION;
    //The location of the sprite on the texture.
    private final Rectangle4i SPRITE;
    //Returns the top left point of the GUI to render at.
    private final Point GUI_LOCATION;
    //Returns the current and max for animation.
    private final Supplier<Pair<Integer, Integer>> animSupplier;
    private final AnimationDirection ANIMATION_DIRECTION;
    private final BooleanSupplier renderPredicate;
    private final Supplier<List<ITextComponent>> tooltipSupplier;

    public DrawableGuiElement(ContainerScreen<?> parent, ResourceLocation spriteLocation, Rectangle4i sprite, Point location, AnimationDirection animDirection, Supplier<Pair<Integer, Integer>> animationSupplier, BooleanSupplier renderPredicate, Supplier<List<ITextComponent>> tooltipSupplier) {
        this.parent = parent;
        SPRITE_LOCATION = spriteLocation;
        SPRITE = sprite;
        GUI_LOCATION = location;
        ANIMATION_DIRECTION = animDirection;
        animSupplier = animationSupplier;
        this.renderPredicate = renderPredicate;
        this.tooltipSupplier = tooltipSupplier;
    }

    public Rectangle4i getBounds() {
        return new Rectangle4i(GUI_LOCATION.x, GUI_LOCATION.y, SPRITE.w, SPRITE.h);
    }

    public void renderTooltip(MatrixStack stack, Point mouse, FontRenderer font) {
        if (tooltipSupplier != null) {
            GuiUtils.drawHoveringText(stack, tooltipSupplier.get(), mouse.x, mouse.y, parent.width, parent.height, -1, font);
        }
    }

    public void draw(MatrixStack stack) {
        if (!renderPredicate.getAsBoolean()) {
            return;
        }
        TextureUtils.changeTexture(SPRITE_LOCATION);
        int x = GUI_LOCATION.x;
        int y = GUI_LOCATION.y;

        Rectangle4i curSprite = new Rectangle4i();
        switch (ANIMATION_DIRECTION) {
            case STATIC: {
                curSprite.set(SPRITE.x, SPRITE.y, SPRITE.w, SPRITE.h);
                break;
            }
            case TOP_DOWN: {
                Pair<Integer, Integer> animContext = animSupplier.get();
                int s = scale(animContext.getLeft(), animContext.getRight(), SPRITE.h);
                curSprite.set(SPRITE.x, SPRITE.y, SPRITE.w, s);
                break;
            }
            case BOTTOM_UP: {
                Pair<Integer, Integer> animContext = animSupplier.get();
                int s = scale(animContext.getLeft(), animContext.getRight(), SPRITE.h);
                y = y + (SPRITE.h - s);
                curSprite.set(SPRITE.x, SPRITE.y + (SPRITE.h - s), SPRITE.w, s);
                break;
            }
            case LEFT_RIGHT: {
                Pair<Integer, Integer> animContext = animSupplier.get();
                int s = scale(animContext.getLeft(), animContext.getRight(), SPRITE.w);
                curSprite.set(SPRITE.x, SPRITE.y, s, SPRITE.h);
                break;
            }
            case RIGHT_LEFT: {
                Pair<Integer, Integer> animContext = animSupplier.get();
                int s = scale(animContext.getLeft(), animContext.getRight(), SPRITE.w);
                x = x + (SPRITE.w - s);
                curSprite.set(SPRITE.x + (SPRITE.w - s), SPRITE.y, s, SPRITE.h);
                break;
            }
        }

        parent.blit(stack, x, y, curSprite.x, curSprite.y, curSprite.w, curSprite.h);
    }

    private static float scaleF(float num, float max, float pixels) {
        return num * pixels / max;
    }

    private static int scale(int num, int max, int pixels) {
        return num * pixels / max;
    }

    public enum AnimationDirection {
        STATIC,
        TOP_DOWN,
        BOTTOM_UP,
        LEFT_RIGHT,
        RIGHT_LEFT;
    }

}
