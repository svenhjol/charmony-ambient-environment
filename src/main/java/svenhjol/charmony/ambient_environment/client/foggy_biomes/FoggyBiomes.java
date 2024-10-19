package svenhjol.charmony.ambient_environment.client.foggy_biomes;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.WinterDropBiomes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector4f;
import svenhjol.charmony.ambient_environment.AmbientEnvironment;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.core.helper.WorldHelper;

import java.util.List;

@FeatureDefinition(side = Side.Client)
public final class FoggyBiomes extends SidedFeature {
    private float trackStart = 172f;
    private float trackEnd = 190f;
    private float trackRed = 0f;
    private float trackGreen = 0f;
    private float trackBlue = 0f;
    private float trackAlpha = 0f;
    private float trackDarkness = 0f;
    private Camera camera;

    public FoggyBiomes(Mod mod) {
        super(mod);
    }

    public static FoggyBiomes feature() {
        return AmbientEnvironment.instance().feature(FoggyBiomes.class);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Vector4f colorCallback(Vector4f original) {
//        var entity = camera.getEntity();
//
//        if (!(entity instanceof LocalPlayer localPlayer)) {
//            return original;
//        }
        return original;
    }

    public boolean skyPassCallback(SkyRenderer skyRenderer) {
        var entity = camera.getEntity();

        if (!(entity instanceof LocalPlayer localPlayer)) {
            return false;
        }

        var level = localPlayer.level();
        var pos = localPlayer.blockPosition();
        var biome = level.getBiome(pos);
        if (biome.is(WinterDropBiomes.PALE_GARDEN) && !level.canSeeSky(pos)) {
            return false;
        }

        return false;
    }

    public float darknessCallback(float original) {
//        var minecraft = Minecraft.getInstance();
//        var player = minecraft.player;
//        if (player == null) return original;
//
//        var level = player.level();
//        var pos = player.blockPosition();
//        var biome = level.getBiome(pos);
//
//        if (biome.is(WinterDropBiomes.PALE_GARDEN)
//            && !level.canSeeSky(pos)) {
//            return 1f;
//        }

        return original;
    }

    public float darknessScaleCallback(float original) {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (player == null) return original;

        var level = player.level();
        var pos = player.blockPosition();
        var biome = level.getBiome(pos);

        if (biome.is(WinterDropBiomes.PALE_GARDEN) && WorldHelper.isOutside(player)
            && checkBlocksAbove(player, List.of(Blocks.PALE_OAK_LEAVES, Blocks.PALE_OAK_LOG, Blocks.PALE_HANGING_MOSS))) {
            trackDarkness = fade(trackDarkness, level.isDay() ? 0.4f : 0.05f, 0.001f);
        } else if (biome.is(Biomes.DARK_FOREST) && WorldHelper.isOutside(player)
            && checkBlocksAbove(player, List.of(Blocks.DARK_OAK_LEAVES, Blocks.DARK_OAK_LOG, Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK))) {
            trackDarkness = fade(trackDarkness, level.isDay() ? 0.22f : 0.05f, 0.001f);
        } else {
            trackDarkness = fade(trackDarkness, original, 0.002f);
        }

        return trackDarkness;
    }

    public FogParameters paramsCallback(FogRenderer.FogMode fogMode, FogParameters original) {
        var entity = camera.getEntity();

        if (!(entity instanceof LocalPlayer player)) {
            return original;
        }
        if (fogMode != FogRenderer.FogMode.FOG_TERRAIN) {
            return original;
        }

        var level = player.level();
        var pos = player.blockPosition();
        var biome = level.getBiome(pos);
        var data = new FogRenderer.FogData(fogMode);
        data.shape = original.shape();

        if (biome.is(WinterDropBiomes.PALE_GARDEN) && WorldHelper.isOutside(player)) {
            track(0f, 60f, original.red(), original.green(), original.blue(), original.alpha(), 0.05f);
        } else {
            track(original.start(), original.end(), original.red(), original.green(), original.blue(), original.alpha());
        }

        return new FogParameters(trackStart, trackEnd, data.shape, trackRed, trackGreen, trackBlue, trackAlpha);
    }

    public void track(float start, float end, float r, float g, float b, float a) {
        track(start, end, r, g, b, a, 0.2f);
    }

    public void track(float start, float end, float r, float g, float b, float a, float speed) {
        trackStart = fade(trackStart, start, speed);
        trackEnd = fade(trackEnd, end, speed);
        trackRed = fade(trackRed, r, speed);
        trackGreen = fade(trackGreen, g, speed);
        trackBlue = fade(trackBlue, b, speed);
        trackAlpha = fade(trackAlpha, a, speed);
    }

    private float fade(float val, float dest) {
        return fade(val, dest, 0.2f);
    }

    private float fade(float val, float dest, float speed) {
        if (val > dest + speed) {
            val -= speed;
        } else if (val < dest - speed) {
            val += speed;
        } else {
            val = dest;
        }

        return val;
    }

    private boolean checkBlocksAbove(Player player, List<Block> blocks) {
        var level = player.level();
        var foundAbove = false;
        int count = 24;
        int start = 1;

        BlockPos playerPos = player.blockPosition();
        for(int i = start; i < start + count; ++i) {
            BlockPos check = new BlockPos(playerPos.getX(), playerPos.getY() + i, playerPos.getZ());
            BlockState state = level.getBlockState(check);
            Block block = state.getBlock();
            if (foundAbove && level.canSeeSky(check)) {
                return true;
            }

            if (!level.isEmptyBlock(check) && blocks.contains(block)) {
                if (level.canSeeSky(check)) {
                    return true;
                }

                if (level.canSeeSkyFromBelowWater(check)) {
                    return true;
                }

                foundAbove = true;
            }
        }

        return false;
    }
}
