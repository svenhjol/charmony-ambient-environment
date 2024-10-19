package svenhjol.charmony.ambient_environment.mixins.foggy_biomes;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.ambient_environment.client.foggy_biomes.FoggyBiomes;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow @Final private SkyRenderer skyRenderer;

    @Inject(
        method = "addSkyPass",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookAddSkyPass(FrameGraphBuilder frameGraphBuilder, Camera camera, float f, FogParameters fogParameters, CallbackInfo ci) {
        if (FoggyBiomes.feature().skyPassCallback(this.skyRenderer)) {
            ci.cancel();
        }
    }
}
