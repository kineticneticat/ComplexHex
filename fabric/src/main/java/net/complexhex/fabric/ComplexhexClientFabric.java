package net.complexhex.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.complexhex.ComplexhexClient;

/**
 * Fabric client loading entrypoint.
 */
public class ComplexhexClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ComplexhexClient.init();
    }
}
