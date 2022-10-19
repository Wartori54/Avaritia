package morph.avaritia.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

public class ModelRendererWing extends ModelRenderer {

//    public ModelRendererWing(Model model, String string) {
//        super(model, string);
//    }

    public ModelRendererWing(Model model) {
        super(model);
    }

    public ModelRendererWing(Model model, int x, int y) {
        super(model, x, y);
    }

    public ModelRendererWing(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void render(MatrixStack stack, IVertexBuilder vertexBuilder, int p_228308_3_, int p_228308_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {
        if (this.visible) {
            //GL11.glCullFace(GL11.GL_BACK);
            RenderSystem.enableCull();
        }
        super.render(stack, vertexBuilder, p_228308_3_, p_228308_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
        if (this.visible) {
            //GL11.glCullFace(GL11.GL_NONE);
            RenderSystem.disableCull();
        }
    }
}
