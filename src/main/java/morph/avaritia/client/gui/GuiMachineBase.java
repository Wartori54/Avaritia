package morph.avaritia.client.gui;

import morph.avaritia.container.ContainerMachineBase;
import morph.avaritia.tile.TileMachineBase;
import net.covers1624.lib.gui.GuiAnimBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by covers1624 on 21/05/2017.
 */
public abstract class GuiMachineBase<C extends ContainerMachineBase> extends GuiAnimBase<C> {

    protected final C container;

    public GuiMachineBase(C container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.container = container;
    }

    protected static float scaleF(float num, float max, float pixels) {
        return num * pixels / max;
    }

    protected static int scale(int num, int max, int pixels) {
        return num * pixels / max;
    }
}
