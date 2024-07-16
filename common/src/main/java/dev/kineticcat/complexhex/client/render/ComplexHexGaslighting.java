package dev.kineticcat.complexhex.client.render;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.kineticcat.complexhex.Complexhex.id;
import static dev.kineticcat.complexhex.client.render.CoolerGaslightingTracker.GASLIGHTS;

public class ComplexHexGaslighting {
    public static void init() {}
    public static final CoolerGaslightingTracker BURNT_AMETHYST_GASLIGHT = gaslight(new CoolerGaslightingTracker(40, id("burnt_variants")));

    public static CoolerGaslightingTracker gaslight(CoolerGaslightingTracker gaslight) {
        CoolerGaslightingTracker old = GASLIGHTS.put(gaslight.GASLIGHTING_PRED, gaslight);
        if (old != null) {
            throw new IllegalArgumentException("complexhex's dev's a dumbass: " + gaslight.GASLIGHTING_PRED);
        }
        return gaslight;
    }
}
