package net.complexhex.registry;



import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.complexhex.Complexhex;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;

import static net.complexhex.Complexhex.id;

public class ComplexhexPatternRegistry {

    private static List<Pattern> PATTERNS = new ArrayList<Pattern>();

    private class Pattern {
        public ResourceLocation id;
        public ActionRegistryEntry are;

        public Pattern(String pat, HexDir dir, String name, Action act) {
            this.id = new ResourceLocation(Complexhex.MOD_ID, name);
            this.are = new ActionRegistryEntry(HexPattern.fromAngles(pat, dir), act);
            PATTERNS.add(this);
        }
        public void register() {
            Registry.register(HexActions.REGISTRY, id, are );
        }
    }




    public static void init() {
        for (Pattern pattern : PATTERNS) {
            pattern.register();
        }
    }
}
