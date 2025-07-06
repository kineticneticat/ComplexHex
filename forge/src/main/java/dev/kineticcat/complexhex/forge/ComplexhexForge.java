package dev.kineticcat.complexhex.forge;

import java.util.Map;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.api.casting.castables.SpecialHandler;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.common.lib.hex.HexArithmetics;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.architectury.platform.forge.EventBuses;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes;
import dev.kineticcat.complexhex.casting.ComplexHexSpecialHandlers;
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry;
import dev.kineticcat.complexhex.casting.arithmetic.ComplexHexArithmetic;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

/**
 * This is your loading entrypoint on forge, in case you need to initialize
 * something platform-specific.
 */
@Mod(Complexhex.MOD_ID)
public class ComplexhexForge {
    public ComplexhexForge() {
        // Submit our event bus to let architectury register our content on the right time
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Complexhex.MOD_ID, bus);
        bus.addListener(ComplexhexClientForge::init);

        addRegisterListener(bus, HexIotaTypes.REGISTRY, ComplexHexIotaTypes.getTypes());
        addRegisterListener(bus, HexActions.REGISTRY, ComplexhexPatternRegistry.getPatterns());
        addRegisterListener(bus, HexArithmetics.REGISTRY, ComplexHexArithmetic.getArithmetics());
        addRegisterListener(bus, IXplatAbstractions.INSTANCE.getSpecialHandlerRegistry(), ComplexHexSpecialHandlers.getSpecialHandlers());

        Complexhex.init();
    }

    private static <T> void addRegisterListener(IEventBus bus, Registry<T> registry, Map<ResourceLocation, T> resources) {
        bus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(registry.key())) {
                for (Map.Entry<ResourceLocation, T> entry : resources.entrySet()) {
                    event.register(registry.key(), helper -> 
                        helper.register(entry.getKey(), entry.getValue())
                    );
                }
            }
        });        
    }
}
