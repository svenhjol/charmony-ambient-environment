package svenhjol.charmony.ambient_environment.mixins;

import svenhjol.charmony.ambient_environment.AmbientEnvironment;
import svenhjol.charmony.core.mixins.BaseMixinConfig;

public class MixinConfig extends BaseMixinConfig {
    @Override
    protected String modId() {
        return AmbientEnvironment.ID;
    }

    @Override
    protected String rootClassPath() {
        return "svenhjol.charmony.ambient_environment";
    }
}
