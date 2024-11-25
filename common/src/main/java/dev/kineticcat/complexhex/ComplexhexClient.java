package dev.kineticcat.complexhex;

import dev.kineticcat.complexhex.client.RegisterClientStuff;

/**
 * Common client loading entrypoint.
 */
public class ComplexhexClient {

//    public static ManagedShaderEffect mse = ShaderEffectManager.getInstance().manage(id("shaders/post/laggingmask.json"));
    public static void init() {
        // what the fuck am i doing
        RegisterClientStuff.init();
    }
}
