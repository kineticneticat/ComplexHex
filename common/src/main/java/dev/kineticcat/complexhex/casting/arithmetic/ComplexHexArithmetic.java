package dev.kineticcat.complexhex.casting.arithmetic;

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexArithmetics;
import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class ComplexHexArithmetic {

    public static HexPattern SINH = HexPattern.fromAngles("eqqqqqaaw", HexDir.EAST);
    public static HexPattern COSH = HexPattern.fromAngles("eqqqqqadw", HexDir.EAST);
    public static HexPattern TANH = HexPattern.fromAngles("ewqqqqqadqe", HexDir.SOUTH_EAST);
    public static HexPattern ASINH = HexPattern.fromAngles("wddeeeeeq", HexDir.SOUTH_EAST);
    public static HexPattern ACOSH = HexPattern.fromAngles("wadeeeeeq", HexDir.NORTH_EAST);
    public static HexPattern ATANH = HexPattern.fromAngles("qeadeeeeewq", HexDir.EAST);
    private static final Map<ResourceLocation, Arithmetic> ARITHMETICS = new LinkedHashMap<>();

    public static void init() {
        for (Map.Entry<ResourceLocation, Arithmetic> entry : ARITHMETICS.entrySet()) {
            Registry.register(HexArithmetics.REGISTRY, entry.getKey(), entry.getValue());
        }
    }

    public static ComplexArithmetic COMPLEX = make(ComplexArithmetic.INSTANCE.arithName(), ComplexArithmetic.INSTANCE);
    public static QuaternionArithmetic QUATERNION = make(QuaternionArithmetic.INSTANCE.arithName(), QuaternionArithmetic.INSTANCE);
    public static LongArithmetic LONG = make(LongArithmetic.INSTANCE.arithName(), LongArithmetic.INSTANCE);
    public static ExprArithmetic EXPR = make(ExprArithmetic.INSTANCE.arithName(), ExprArithmetic.INSTANCE);

    private static <T extends Arithmetic> T make(String name, T arithmetic) {
        var old = ARITHMETICS.put(new ResourceLocation(Complexhex.MOD_ID, name), arithmetic);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return arithmetic;
    }

}
