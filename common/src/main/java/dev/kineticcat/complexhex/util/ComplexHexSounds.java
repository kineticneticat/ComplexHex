package dev.kineticcat.complexhex.util;

import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ComplexHexSounds {

    public static void registerSounds(BiConsumer<SoundEvent, ResourceLocation> r) {
        for (var e : SOUNDS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
    private static final Map<ResourceLocation, SoundEvent> SOUNDS = new LinkedHashMap<>();

    public static SoundEvent HEXBOX_SCRATCH =  register("block.hexbox.scratch");

    private static SoundEvent register(String name) {
        var id = Complexhex.id(name);
        var sound = SoundEvent.createVariableRangeEvent(id);
        var old = SOUNDS.put(id, sound);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return sound;
    }
}
