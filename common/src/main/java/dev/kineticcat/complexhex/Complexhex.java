package dev.kineticcat.complexhex;

import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes;
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry;
import dev.kineticcat.complexhex.casting.arithmetic.ComplexHexArithmetic;
import dev.kineticcat.complexhex.client.render.ComplexHexGaslighting;
import dev.kineticcat.complexhex.client.render.CoolerGaslightingTracker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class Complexhex {
    public static final String MOD_ID = "complexhex";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public static void init() {
        LOGGER.info("Complex Hex says 'ough'");

        ComplexhexAbstractions.initPlatformSpecific();
        ComplexHexIotaTypes.init();
        ComplexhexPatternRegistry.init();
        ComplexHexArithmetic.init();
        ComplexHexGaslighting.init();

        LOGGER.info(ComplexhexAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    public static ResourceLocation id(String string) {
        return new ResourceLocation(MOD_ID, string);
    }
    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<T> registry) {
        return (t, resourceLocation) -> Registry.register(registry, resourceLocation, t);
    }
}
