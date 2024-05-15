package net.complexhex.casting.patterns;



import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.complexhex.Complexhex;
import net.complexhex.api.casting.iota.complex.ComplexNumberIota;
import net.complexhex.casting.arithmetic.OpSignum;
import net.complexhex.casting.spells.OpCongrats;
import net.complexhex.stuff.ComplexNumber;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;


import java.util.LinkedHashMap;
import java.util.Map;

public class ComplexhexPatternRegistry {

    private static final Map<ResourceLocation, ActionRegistryEntry> PATTERNS = new LinkedHashMap<>();

    public static final HexPattern CONGRATS = make("eed", HexDir.WEST, "congrats", new OpCongrats());
    public static final HexPattern CONST$COMPLEX$I$ = make("edd", HexDir.NORTH_WEST, "const/complex/i",
            Action.makeConstantOp(new ComplexNumberIota(new ComplexNumber(0, 1))));

    public static void init() {
        for (Map.Entry<ResourceLocation, ActionRegistryEntry> entry : PATTERNS.entrySet()) {
            Registry.register(HexActions.REGISTRY, entry.getKey(), entry.getValue());
        }
    }
    private static HexPattern make(String pattern, HexDir dir, String name, Action act ) {
        PATTERNS.put(
                new ResourceLocation(Complexhex.MOD_ID, name),
                new ActionRegistryEntry(HexPattern.fromAngles(pattern, dir), act)
        );
        return HexPattern.fromAngles(pattern, dir);
    }
}
