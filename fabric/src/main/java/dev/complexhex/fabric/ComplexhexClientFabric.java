package dev.complexhex.fabric;

import dev.kineticcat.complexhex.ComplexhexClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * Fabric client loading entrypoint.
 */
public class ComplexhexClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ComplexhexClient.init();
    }
}
