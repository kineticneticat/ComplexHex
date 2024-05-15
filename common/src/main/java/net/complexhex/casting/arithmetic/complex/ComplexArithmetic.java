package net.complexhex.casting.arithmetic.complex;

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException;
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator;
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary;
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate;
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import net.complexhex.api.casting.iota.complex.ComplexHexIotaTypes;
import net.complexhex.api.casting.iota.complex.ComplexNumberIota;
import net.complexhex.casting.arithmetic.complex.OpComplexAdd;

import java.util.List;
import java.util.function.BiFunction;

public class ComplexArithmetic implements Arithmetic {
    @Override
    public String arithName() {
        return "complex_maths";
    }
    public static final HexPattern CMUL = HexPattern.fromAngles("", HexDir.EAST);
    public static final HexPattern ARG = HexPattern.fromAngles("", HexDir.EAST);
    public static final HexPattern REAL = HexPattern.fromAngles("", HexDir.EAST);
    public static final HexPattern IMAG = HexPattern.fromAngles("", HexDir.EAST);
    public static final List<HexPattern> OPS = List.of(
            ADD,
            SUB,
            MUL,
            DIV,
            CMUL,
            ARG,
            REAL,
            IMAG

    );
    private static final IotaType<ComplexNumberIota> COMPLEXNUMBER = ComplexHexIotaTypes.COMPLEXNUMBER;
    public static final IotaMultiPredicate ACCEPTS = IotaMultiPredicate.all(IotaPredicate.ofType(COMPLEXNUMBER));
    @Override
    public Iterable<HexPattern> opTypes() {
        return OPS;
    }

    @Override
    public Operator getOperator(HexPattern pattern) {
        if (pattern.equals(ADD)) {
            return new OpComplexAdd();
        } else {
            throw new InvalidOperatorException(pattern + " is not valid for complex arithmetic");
        }
    }
}
