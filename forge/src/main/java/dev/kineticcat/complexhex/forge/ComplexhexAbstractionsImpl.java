package dev.kineticcat.complexhex.forge;

import dev.kineticcat.complexhex.ComplexhexAbstractions;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ComplexhexAbstractionsImpl {
    /**
     * This is the actual implementation of {@link ComplexhexAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
	
    public static void initPlatformSpecific() {
        ComplexhexConfigForge.init();
    }
}
