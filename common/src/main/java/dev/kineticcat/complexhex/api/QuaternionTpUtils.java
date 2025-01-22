package dev.kineticcat.complexhex.api;

import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.stuff.Quaternion;
import kotlin.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuaternionTpUtils {
    public static Quaternion getQuaternionPosition(ResourceKey<Level> dim, Long seed) {

        int hash = dim.location().hashCode();
        long wawa = seed * hash;
        return new Quaternion(
                (wawa & 0b1111111111111111000000000000000000000000000000000000000000000000L) >> 48,
                (wawa & 0b0000000000000000111111111111111100000000000000000000000000000000L) >> 32,
                (wawa & 0b0000000000000000000000000000000011111111111111110000000000000000L) >> 16,
                (wawa & 0b0000000000000000000000000000000000000000000000001111111111111111L)
        ).Qdiv(1000.0);
    }

    public static Quaternion getDimDelta(ResourceKey<Level> source, ResourceKey<Level> target, Long seed) {
        return getQuaternionPosition(target, seed)
                .Qsub(getQuaternionPosition(source, seed));
    }

    public static Pair<ResourceKey<Level>, Vec3> getTarget(ResourceKey<Level> source, Quaternion delta, Long seed, MinecraftServer server) {
        // delta should be dimdelta + posdelta
        // therefor if it uses a valid dim delta
        // delta.w should equal dimdelta.w
        List<ResourceKey<Level>> possibleTargets = new ArrayList<>((Collection<ServerLevel>) server.getAllLevels()).stream().map(Level::dimension).toList();
        for (ResourceKey<Level> target : possibleTargets) {
            Quaternion dimdelta = getDimDelta(source, target, seed);
            Quaternion posdelta = delta.Qsub(dimdelta);
            Complexhex.LOGGER.info(delta);
            Complexhex.LOGGER.info(dimdelta);
            Complexhex.LOGGER.info(posdelta);
            if (DoubleIota.tolerates(posdelta.w, 0)) {
                return new Pair<>(target, posdelta.imaginaryAsVec3());
            }
        }
        return null;
    }
}