package svenhjol.charmony.ambient_environment;

import svenhjol.charmony.core.annotations.ModDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.enums.Side;

@ModDefinition(id = AmbientEnvironment.ID, sides = {Side.Client},
    name = "Ambient environment",
    description = "Simple environment and atmospheric effects for Minecraft.")
public class AmbientEnvironment extends Mod {
    public static final String ID = "charmony-ambient-environment";
    private static AmbientEnvironment instance;

    public static AmbientEnvironment instance() {
        if (instance == null) {
            instance = new AmbientEnvironment();
        }
        return instance;
    }
}
