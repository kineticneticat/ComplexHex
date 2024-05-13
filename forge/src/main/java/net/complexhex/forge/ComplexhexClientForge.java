package net.complexhex.forge;

import net.complexhex.ComplexhexClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Forge client loading entrypoint.
 */
public class ComplexhexClientForge {
    public static void init(FMLClientSetupEvent event) {
        ComplexhexClient.init();
    }
}
