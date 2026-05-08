package dev.kineticcat.complexhex.casting;


import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.castables.OperationAction;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntitiesBy;
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntityAt;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.casting.actions.*;
import dev.kineticcat.complexhex.casting.actions.bits.*;
import dev.kineticcat.complexhex.casting.actions.expr.*;
import dev.kineticcat.complexhex.casting.actions.mathematics.complex.OpArgument;
import dev.kineticcat.complexhex.casting.actions.mathematics.complex.OpConjugate;
import dev.kineticcat.complexhex.casting.actions.mathematics.complex.OpImaginary;
import dev.kineticcat.complexhex.casting.actions.mathematics.complex.OpReal;
import dev.kineticcat.complexhex.casting.actions.mathematics.expr.OpExprEquals;
import dev.kineticcat.complexhex.casting.actions.mathematics.longs.OpArithmeticRightBitshift;
import dev.kineticcat.complexhex.casting.actions.mathematics.longs.OpLeftBitshift;
import dev.kineticcat.complexhex.casting.actions.mathematics.longs.OpLogicalRightBitshift;
import dev.kineticcat.complexhex.casting.actions.mathematics.longs.OpNumLong;
import dev.kineticcat.complexhex.casting.actions.mathematics.quaternion.OpGetQuaternionComponent;
import dev.kineticcat.complexhex.casting.actions.mathematics.quaternion.OpQInvert;
import dev.kineticcat.complexhex.casting.actions.mathematics.quaternion.OpQMake;
import dev.kineticcat.complexhex.casting.actions.mathematics.quaternion.OpQUnmake;
import dev.kineticcat.complexhex.stuff.ComplexNumber;
import dev.kineticcat.complexhex.stuff.Quaternion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
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
    public static final HexPattern REAL = make("deew", HexDir.SOUTH_EAST, "real",
            OpReal.INSTANCE);
    public static final HexPattern IMAGINARY = make("eew", HexDir.WEST, "imaginary",
            OpImaginary.INSTANCE);
    public static final HexPattern CONJUGATE = make("wqqd", HexDir.SOUTH_WEST, "conjugate",
            OpConjugate.INSTANCE);
    public static final HexPattern CNARG = make("waqqqqqeww", HexDir.SOUTH_EAST, "cnarg",
            OpArgument.INSTANCE);

    // Quaternion Arithmetic
    public static final HexPattern CONST$QUAT$1$ = make("waqqqqqea", HexDir.SOUTH_EAST, "const/quaternion/1",
            Action.makeConstantOp(new Quaternion(1, 0, 0, 0).asIota()));
    public static final HexPattern CONST$QUAT$I$ = make("waqqqqqeq", HexDir.SOUTH_EAST, "const/quaternion/i",
            Action.makeConstantOp(new Quaternion(0, 1, 0, 0).asIota()));
    public static final HexPattern CONST$QUAT$J$ = make("waqqqqqee", HexDir.SOUTH_EAST, "const/quaternion/j",
            Action.makeConstantOp(new Quaternion(0, 0, 1, 0).asIota()));
    public static final HexPattern CONST$QUAT$K$ = make("waqqqqqed", HexDir.SOUTH_EAST, "const/quaternion/k",
            Action.makeConstantOp(new Quaternion(0, 0, 0, 1).asIota()));
    public static final HexPattern QINVERT = make("waqqqqqew", HexDir.SOUTH_EAST, "qinvert",
            OpQInvert.INSTANCE);
    public static final HexPattern QW = make("wdeeeeeqa", HexDir.SOUTH_EAST, "qw",
            new OpGetQuaternionComponent(OpGetQuaternionComponent.Component.W));
    public static final HexPattern QX = make("wdeeeeeqq", HexDir.SOUTH_EAST, "qx",
            new OpGetQuaternionComponent(OpGetQuaternionComponent.Component.X));
    public static final HexPattern QY = make("wdeeeeeqe", HexDir.SOUTH_EAST, "qy",
            new OpGetQuaternionComponent(OpGetQuaternionComponent.Component.Y));
    public static final HexPattern QZ = make("wdeeeeeqd", HexDir.SOUTH_EAST, "qz",
            new OpGetQuaternionComponent(OpGetQuaternionComponent.Component.Z));
    public static final HexPattern QMAKE = make("waqqqqqe", HexDir.SOUTH_EAST, "qmake",
            OpQMake.INSTANCE);
    public static final HexPattern QUNMAKE = make("wdeeeeeq", HexDir.SOUTH_EAST, "qunmake",
            OpQUnmake.INSTANCE);
    public static final HexPattern QUATTOMAT = make("wdeeeeeqeawwaeaww", HexDir.SOUTH_EAST, "quattomat",
            OpQuaternionToMatrix.INSTANCE);
    public static final HexPattern MATTOQUAT = make("waqqqqqeeawwaeaww", HexDir.SOUTH_EAST, "mattoquat",
            OpMatrixToQuaternion.INSTANCE);
    public static final HexPattern AXISANGLE = make("waqqqqqedaqqqa", HexDir.SOUTH_EAST, "axisangle",
            OpAxisAngle.INSTANCE);

    // BITs
    public static final HexPattern SUMMONBLOCKDISPLAY = make("wqwqwqwqwqwawqaqqqqqe", HexDir.SOUTH_EAST, "summonblockdisplay",
            OpSummonBlockDisplay.INSTANCE);
    public static final HexPattern SUMMONITEMDISPLAY = make("wqwqwqwqwqwaqedeaaedeq", HexDir.SOUTH_EAST, "summonitemdisplay",
            OpSummonItemDisplay.INSTANCE);
    public static final HexPattern SUMMONTEXTDISPLAY = make("wqwqwqwqwqwawaaqawdeddw", HexDir.SOUTH_EAST, "summontextdisplay",
            OpSummonTextDisplay.INSTANCE);
    public static final HexPattern KILLBIT = make("wqwqwqwqwqwaqdwddwdq", HexDir.SOUTH_EAST, "killbit",
            OpKillBIT.INSTANCE);
    public static final HexPattern UPDATEBIT = make("wqwqwqwqwqwaqedwaqe", HexDir.SOUTH_EAST, "update_bit",
            OpUpdateBIT.INSTANCE);

    public static final HexPattern TRANSLATEBIT = make("wqwqwqwqwqwawwaqaeaqe", HexDir.SOUTH_EAST, "translatebit",
            OpTranslateBIT.INSTANCE);
    public static final HexPattern ROTATEBIT = make("wqwqwqwqwqwaqeeeeedww", HexDir.SOUTH_EAST, "rotatebit",
            OpRotateBIT.INSTANCE);
    public static final HexPattern SCALEBIT = make("wqwqwqwqwqwawwaawaawa", HexDir.SOUTH_EAST, "scalebit",
            OpScaleBIT.INSTANCE);

    public static final HexPattern GET4X4MAT = make("qwqwqwqwqwqqeawwaeaww", HexDir.SOUTH_WEST, "get_bit_4x4",
            OpGetBit4x4.INSTANCE);
    public static final HexPattern SET4X4Mat = make("eweweweweweewwdqdwwdq", HexDir.NORTH_EAST, "set_bit_4x4",
            OpSetBit4x4.INSTANCE);

    public static final HexPattern GET_ENTITY$BIT = make("qqwwewewewewewwqqdaqaaww", HexDir.SOUTH_EAST, "get_entity/bit",
            new OpGetEntityAt(e -> e instanceof Display));
    public static final HexPattern ZONE_ENTITY$BIT = make("qqwwewewewewewwqqwdeddww", HexDir.SOUTH_EAST, "zone_entity/bit",
            new OpGetEntitiesBy(e -> e instanceof Display, false));
    public static final HexPattern GET_ENTITY$NOT_BIT = make("eewwqwqwqwqwqwweewaqaaww", HexDir.NORTH_EAST, "zone_entity/not_bit",
            new OpGetEntitiesBy(e -> e instanceof Display, false));

    public static final HexPattern BUBBLE = make("qdqdqdqdqdq", HexDir.SOUTH_EAST, "bubble",
            OpBubbleIota.INSTANCE);

    // ASCII

    public static final HexPattern TO_ASCII = make("eawdwae", HexDir.EAST, "ascii",
            OpASCIIValue.INSTANCE);

    // Longs
    public static final HexPattern TO_LONG = make("wawdedwaaw", HexDir.SOUTH_EAST, "long/convert",
            new OpNumLong(true));
    public static final HexPattern SHIFT$LEFT = make("qqae", HexDir.SOUTH_WEST, "shift/left",
            OpLeftBitshift.INSTANCE);
    public static final HexPattern SHIFT$RIGHT$LOGICAL = make("eaqqdee", HexDir.SOUTH_EAST, "shift/right/logical",
            OpLogicalRightBitshift.INSTANCE);
    public static final HexPattern SHIFT$RIGHT$ARITHMETIC = make("eaqq", HexDir.SOUTH_EAST, "shift/right/arithmetic",
            OpArithmeticRightBitshift.INSTANCE);

    // exprs

    public static final HexPattern NEW_SYMBOL = make("wdwdd", HexDir.SOUTH_EAST, "new_symbol",
            OpNewSymbol.INSTANCE);
    public static final HexPattern SUBSTITUTE = make("wdwddwdw", HexDir.SOUTH_EAST, "substitute",
            OpSubstitute.INSTANCE);
    public static final HexPattern EQUALS = make("wdwddwqad", HexDir.SOUTH_EAST, "equals",
            OpExprEquals.INSTANCE);
    public static final HexPattern PIECEWISE = make("wdwddwawdd", HexDir.SOUTH_EAST, "piecewise",
            OpPiecewise.INSTANCE);
    public static final HexPattern DERIVATIVE = make("wdwddwewddwdw", HexDir.SOUTH_EAST, "derivative",
            OpDerivative.INSTANCE);
    public static final HexPattern MAKE_MATRIX = make("wdwddweawwaeaw", HexDir.SOUTH_EAST, "matrix",
            OpMakeMatrix.INSTANCE);
    public static final HexPattern SUMMON_LINE = make("wdwddwq", HexDir.SOUTH_EAST, "summon/line",
            OpSpawnParametricLine.INSTANCE);
    public static final HexPattern SUMMON_SURFACE = make("wdwddwqq", HexDir.SOUTH_EAST, "summon/surface",
            OpSpawnParametricSurface.INSTANCE);

    public static final HexPattern SINH = make("dqqqqqaaw", HexDir.NORTH_EAST, "sinh");
    public static final HexPattern COSH = make("wqqqqqadw", HexDir.SOUTH_EAST, "cosh");
    public static final HexPattern TANH = make("ewqqqqqadqe", HexDir.SOUTH_EAST, "tanh");
    public static final HexPattern ASINH = make("wddeeeeea", HexDir.SOUTH_EAST, "asinh");
    public static final HexPattern ACOSH = make("wadeeeeew", HexDir.NORTH_EAST, "acosh");
    public static final HexPattern ATANH = make("qeadeeeeewq", HexDir.EAST, "atanh");


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
