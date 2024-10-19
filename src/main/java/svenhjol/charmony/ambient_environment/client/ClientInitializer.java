package svenhjol.charmony.ambient_environment.client;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.ambient_environment.AmbientEnvironment;
import svenhjol.charmony.ambient_environment.client.foggy_biomes.FoggyBiomes;
import svenhjol.charmony.core.enums.Side;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.client.ClientInitializer.init();

        // Prepare and run the mod.
        var ambientEnvironment = AmbientEnvironment.instance();
        ambientEnvironment.addFeature(FoggyBiomes.class);
        ambientEnvironment.run(Side.Client);
    }
}
