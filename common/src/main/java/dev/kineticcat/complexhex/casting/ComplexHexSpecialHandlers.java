package dev.kineticcat.complexhex.casting;

import at.petrak.hexcasting.api.casting.castables.SpecialHandler;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.kineticcat.complexhex.casting.actions.mathematics.longs.SpecialHandlerLongLiteral;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.kineticcat.complexhex.Complexhex.id;

public class ComplexHexSpecialHandlers {
    private static final Map<ResourceLocation, SpecialHandler.Factory<?>> SPECIAL_HANDLERS = new LinkedHashMap<>();

    public static Map<ResourceLocation, SpecialHandler.Factory<?>> getSpecialHandlers() {
        return SPECIAL_HANDLERS;
    }

    public static final SpecialHandler.Factory<SpecialHandlerLongLiteral> LONG = make("long",
            new SpecialHandlerLongLiteral.Factory());

    private static <T extends SpecialHandler> SpecialHandler.Factory<T> make(String name, SpecialHandler.Factory<T> handler) {
        var old = SPECIAL_HANDLERS.put(id(name), handler);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return handler;
    }

//    public static void register(BiConsumer<SpecialHandler.Factory<?>, ResourceLocation> r) {
//        for (var e : SPECIAL_HANDLERS.entrySet()) {
//            r.accept(e.getValue(), e.getKey());
//        }
//    }
}