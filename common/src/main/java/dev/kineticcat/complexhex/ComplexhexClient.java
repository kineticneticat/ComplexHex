package dev.kineticcat.complexhex;

import dev.kineticcat.complexhex.client.RegisterClientStuff;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;

import static dev.kineticcat.complexhex.Complexhex.id;

/**
 * Common client loading entrypoint.
 */
public class ComplexhexClient {

    public static ManagedShaderEffect mse = ShaderEffectManager.getInstance().manage(id("shaders/post/bruhhrubruh.json"));
    public static void init() {
        // what the fuck am i doing
        RegisterClientStuff.init();
    }
}
