package svenhjol.charmony.ambient_environment.mixins.foggy_biomes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charmony.ambient_environment.client.foggy_biomes.FoggyBiomes;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Unique
    private static FogRenderer.FogMode fogMode;

    @Inject(
        method = "setupFog",
        at = @At("HEAD")
    )
    private static void hookSetupFogCapture(Camera camera, FogRenderer.FogMode fogMode, Vector4f vector4f, float f, boolean bl, float g, CallbackInfoReturnable<FogParameters> cir) {
        FoggyBiomes.feature().setCamera(camera);
        FogRendererMixin.fogMode = fogMode;
    }

    @ModifyReturnValue(
        method = "computeFogColor",
        at = @At("RETURN")
    )
    private static Vector4f hookComputeFogColor(Vector4f original) {
        return FoggyBiomes.feature().colorCallback(original);
    }

    @ModifyReturnValue(
        method = "setupFog",
        at = @At(value = "RETURN")
    )
    private static FogParameters hookSetupFog(FogParameters original) {
        return FoggyBiomes.feature().paramsCallback(fogMode, original);
    }
}
