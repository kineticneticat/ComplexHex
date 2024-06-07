package dev.kineticcat.complexhex.casting;



import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.castables.OperationAction;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.stuff.ComplexNumber;
import dev.kineticcat.complexhex.stuff.Quaternion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.LinkedHashMap;
import java.util.Map;

public class ComplexhexPatternRegistry {
    public static final Logger LOGGER = LogManager.getLogger(Complexhex.MOD_ID);
    private static final Map<ResourceLocation, ActionRegistryEntry> PATTERNS = new LinkedHashMap<>();

    //Complex Arithmetic
    public static final HexPattern CONST$COMPLEX$1$ = make("wqqa", HexDir.SOUTH_WEST, "const/complex/1",
            Action.makeConstantOp(new ComplexNumber(1, 0).asIota()));
    public static final HexPattern CONST$COMPLEX$I$ = make("wqq", HexDir.SOUTH_WEST, "const/complex/i",
            Action.makeConstantOp(new ComplexNumber(0, 1).asIota()));
    public static final HexPattern COMPLEXMUL = make("wqqewaqaw", HexDir.SOUTH_WEST, "cmul");
    public static final HexPattern REAL = make("wqqq", HexDir.SOUTH_WEST, "real");
    public static final HexPattern IMAGINARY = make("wqqe", HexDir.SOUTH_WEST, "imaginary");
    public static final HexPattern CONJUGATE = make("wqqd", HexDir.SOUTH_WEST, "conjugate");

    // Quaternion Arithmetic
    public static final HexPattern CONST$QUAT$1$ = make("waqqqqqea", HexDir.SOUTH_EAST, "const/quaternion/1",
            Action.makeConstantOp(new Quaternion(1, 0, 0, 0).asIota()));
    public static final HexPattern CONST$QUAT$I$ = make("waqqqqqeq", HexDir.SOUTH_EAST, "const/quaternion/i",
            Action.makeConstantOp(new Quaternion(0, 1, 0, 0).asIota()));
    public static final HexPattern CONST$QUAT$J$ = make("waqqqqqee", HexDir.SOUTH_EAST, "const/quaternion/j",
            Action.makeConstantOp(new Quaternion(0, 0, 1, 0).asIota()));
    public static final HexPattern CONST$QUAT$K$ = make("waqqqqqed", HexDir.SOUTH_EAST, "const/quaternion/k",
            Action.makeConstantOp(new Quaternion(0, 0, 0, 1).asIota()));
    public static final HexPattern QMUL = make("waqqqqqewaqaw", HexDir.SOUTH_EAST, "qmul");
    public static final HexPattern QINVERT = make("waqqqqqew", HexDir.SOUTH_EAST, "qinvert");
    public static final HexPattern QA = make("waqqqqqeaw", HexDir.SOUTH_EAST, "qa");
    public static final HexPattern QB = make("waqqqqqeqw", HexDir.SOUTH_EAST, "qb");
    public static final HexPattern QC = make("waqqqqqeew", HexDir.SOUTH_EAST, "qc");
    public static final HexPattern QD = make("waqqqqqedw", HexDir.SOUTH_EAST, "qd");
    public static final HexPattern QMAKE = make("waqqqqqe", HexDir.SOUTH_EAST, "qmake");
    public static final HexPattern QUNMAKE = make("wdeeeeeq", HexDir.SOUTH_EAST, "qunmake");

    public static void init() {
        for (Map.Entry<ResourceLocation, ActionRegistryEntry> entry : PATTERNS.entrySet()) {
//            LOGGER.info(entry);
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
