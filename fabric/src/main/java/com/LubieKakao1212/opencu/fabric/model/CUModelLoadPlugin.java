package com.LubieKakao1212.opencu.fabric.model;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.renderer.RepulsorDeviceRenderer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;

public class CUModelLoadPlugin implements ModelLoadingPlugin {

    /**
     * Called towards the beginning of the model loading process, every time resource are (re)loaded.
     * Use the context object to extend model loading as desired.
     *
     * @param pluginContext
     */
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.addModels(
                RepulsorDeviceRenderer.frameLocation
        );
        pluginContext.addModels(
                RepulsorDeviceRenderer.back_models
        );
    }
}
