package morph.avaritia.api.registration;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Implemented on an item for model registration, completely arbitrary.
 */
public interface IModelRegister {

    /**
     * Called when it is time to initialize models in preInit.
     */
    @OnlyIn(Dist.CLIENT)
    void registerModels();

}
