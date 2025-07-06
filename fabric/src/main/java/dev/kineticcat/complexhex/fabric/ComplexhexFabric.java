package dev.kineticcat.complexhex.fabric;

import java.util.Map;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.common.lib.hex.HexArithmetics;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes;
import dev.kineticcat.complexhex.casting.ComplexHexSpecialHandlers;
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry;
import dev.kineticcat.complexhex.casting.arithmetic.ComplexHexArithmetic;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * This is your loading entrypoint on fabric(-likes), in case you need to initialize
 * something platform-specific.
 * <br/>
 * Since quilt can load fabric mods, you develop for two platforms in one fell swoop.
 * Feel free to check out the <a href="https://github.com/architectury/architectury-templates">Architectury templates</a>
 * if you want to see how to add quilt-specific code.
 */
public class ComplexhexFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        Complexhex.init();
        
        initResources(HexIotaTypes.REGISTRY, ComplexHexIotaTypes.getTypes());
        initResources(HexActions.REGISTRY, ComplexhexPatternRegistry.getPatterns());
        initResources(HexArithmetics.REGISTRY, ComplexHexArithmetic.getArithmetics());
        initResources(IXplatAbstractions.INSTANCE.getSpecialHandlerRegistry(), ComplexHexSpecialHandlers.getSpecialHandlers());
    }

    private static <T> void initResources(Registry<T> registry, Map<ResourceLocation, T> resources) {
        for (Map.Entry<ResourceLocation, T> entry : resources.entrySet()) {
            Registry.register(registry, entry.getKey(), entry.getValue());
        }
    }
}
