package dev.kineticcat.complexhex.casting;



import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.castables.OperationAction;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.casting.spells.OpCongrats;
import dev.kineticcat.complexhex.stuff.ComplexNumber;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;


import java.util.LinkedHashMap;
import java.util.Map;

public class ComplexhexPatternRegistry {

    private static final Map<ResourceLocation, ActionRegistryEntry> PATTERNS = new LinkedHashMap<>();

    public static final HexPattern CONGRATS = make("eed", HexDir.WEST, "congrats", new OpCongrats());
    //Complex Arithmetic
    public static final HexPattern CONST$COMPLEX$I$ = make("wqq", HexDir.SOUTH_WEST, "const/complex/i",
            Action.makeConstantOp(new ComplexNumber(0, 1).asIota()));
    public static final HexPattern COMPLEXMUL = make("wqqewaqaw", HexDir.SOUTH_WEST, "complexmul");
    public static final HexPattern REAL = make("wqqq", HexDir.SOUTH_WEST, "real");
    public static final HexPattern IMAGINARY = make("wqqe", HexDir.SOUTH_WEST, "imaginary");
    public static final HexPattern CONJUGATE = make("wqqd", HexDir.SOUTH_WEST, "conjugate");
    // Quaternion Arithmetic
//    public static final HexPattern CONST$QUATERNION$I = make()
    public static void init() {
        for (Map.Entry<ResourceLocation, ActionRegistryEntry> entry : PATTERNS.entrySet()) {
            Registry.register(HexActions.REGISTRY, entry.getKey(), entry.getValue());
        }
    }
    private static HexPattern make(String signature, HexDir dir, String name, Action act ) {
        PATTERNS.put(
                new ResourceLocation(Complexhex.MOD_ID, name),
                new ActionRegistryEntry(HexPattern.fromAngles(signature, dir), act)
        );
        return HexPattern.fromAngles(signature, dir);
    }
    private static HexPattern make(String signature, HexDir dir, String name) {
        HexPattern pattern = HexPattern.fromAngles(signature, dir);
        PATTERNS.put(
                new ResourceLocation(Complexhex.MOD_ID, name),
                new ActionRegistryEntry(pattern, new OperationAction(pattern))
        );
        return pattern;
    }
}
