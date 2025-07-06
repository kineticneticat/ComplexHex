package dev.kineticcat.complexhex.forge;

import java.util.Map;

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
import net.minecraft.resources.ResourceKey;
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

        Complexhex.init();

        bus.addListener((RegisterEvent event) -> {
            registerAll(event, HexIotaTypes.REGISTRY.key(), ComplexHexIotaTypes.getTypes());
            registerAll(event, HexActions.REGISTRY.key(), ComplexhexPatternRegistry.getPatterns());
            registerAll(event, HexArithmetics.REGISTRY.key(), ComplexHexArithmetic.getArithmetics());
            registerAll(event, IXplatAbstractions.INSTANCE.getSpecialHandlerRegistry().key(), ComplexHexSpecialHandlers.getSpecialHandlers());
        });
    }

    private static <T> void registerAll(RegisterEvent event, ResourceKey<? extends Registry<T>> key, Map<ResourceLocation, T> entries) {
        if (event.getRegistryKey().equals(key)) {
            entries.forEach((id, value) -> event.register(key, helper -> helper.register(id, value)));
        }
    }
}
