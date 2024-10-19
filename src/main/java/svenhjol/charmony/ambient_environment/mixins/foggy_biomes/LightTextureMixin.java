package svenhjol.charmony.ambient_environment.mixins.foggy_biomes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charmony.ambient_environment.client.foggy_biomes.FoggyBiomes;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    @ModifyReturnValue(
        method = "getDarknessGamma",
        at = @At("RETURN")
    )
    private float hookGetDarknessGamma(float original) {
        return FoggyBiomes.feature().darknessCallback(original);
    }

    @ModifyReturnValue(
        method = "calculateDarknessScale",
        at = @At("RETURN")
    )
    private float calculateDarknessScale(float original) {
        return FoggyBiomes.feature().darknessScaleCallback(original);
    }
}
