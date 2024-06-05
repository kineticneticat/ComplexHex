package dev.complexhex.fabric;

import dev.kineticcat.complexhex.ComplexhexAbstractions;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ComplexhexAbstractionsImpl {
    /**
     * This is the actual implementation of {@link ComplexhexAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
	
    public static void initPlatformSpecific() {
        ComplexhexConfigFabric.init();
    }
}
