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
import dev.kineticcat.complexhex.stuff.Quaternion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;


import java.util.LinkedHashMap;
import java.util.Map;

public class ComplexhexPatternRegistry {

    private static final Map<ResourceLocation, ActionRegistryEntry> PATTERNS = new LinkedHashMap<>();

    public static final HexPattern CONGRATS = make("eed", HexDir.WEST, "congrats", new OpCongrats());
    //Complex Arithmetic
    public static final HexPattern CONST$COMPLEX$1$ = make("wqqa", HexDir.SOUTH_WEST, "const/complex/1",
            Action.makeConstantOp(new ComplexNumber(1, 0).asIota()));
    public static final HexPattern CONST$COMPLEX$I$ = make("wqq", HexDir.SOUTH_WEST, "const/complex/i",
            Action.makeConstantOp(new ComplexNumber(0, 1).asIota()));
    public static final HexPattern COMPLEXMUL = make("wqqewaqaw", HexDir.SOUTH_WEST, "complexmul");
    public static final HexPattern REAL = make("wqqq", HexDir.SOUTH_WEST, "real");
    public static final HexPattern IMAGINARY = make("wqqe", HexDir.SOUTH_WEST, "imaginary");
    public static final HexPattern CONJUGATE = make("wqqd", HexDir.SOUTH_WEST, "conjugate");

    // Quaternion Arithmetic
    public static final HexPattern CONST$QUAT$1$ = make("deeeeeqa", HexDir.SOUTH_EAST, "const/quaternion/1",
            Action.makeConstantOp(new Quaternion(1, 0, 0, 0).asIota()));
    public static final HexPattern CONST$QUAT$I$ = make("deeeeeqq", HexDir.SOUTH_EAST, "const/quaternion/i",
            Action.makeConstantOp(new Quaternion(0, 1, 0, 0).asIota()));
    public static final HexPattern CONST$QUAT$J$ = make("deeeeeqe", HexDir.SOUTH_EAST, "const/quaternion/j",
            Action.makeConstantOp(new Quaternion(0, 0, 1, 0).asIota()));
    public static final HexPattern CONST$QUAT$K$ = make("deeeeeqd", HexDir.SOUTH_EAST, "const/quaternion/k",
            Action.makeConstantOp(new Quaternion(0, 0, 0, 1).asIota()));
    public static final HexPattern QMUL = make("deeeeeqwaqaw", HexDir.SOUTH_EAST, "qmul");
    public static final HexPattern QINVERT = make("deeeeeqw", HexDir.SOUTH_EAST, "qinvert");
    public static final HexPattern QA = make("deeeeeqaw", HexDir.SOUTH_EAST, "qa");
    public static final HexPattern QB = make("deeeeeqqw", HexDir.SOUTH_EAST, "qb");
    public static final HexPattern QC = make("deeeeeqew", HexDir.SOUTH_EAST, "qc");
    public static final HexPattern QD = make("deeeeeqdw", HexDir.SOUTH_EAST, "qd");
    public static final HexPattern QMAKE = make("deeeeeq", HexDir.SOUTH_EAST, "qmake");
    public static final HexPattern QUNMAKE = make("aqqqqqe", HexDir.SOUTH_EAST, "qunmake");

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
