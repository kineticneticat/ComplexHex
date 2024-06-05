package net.complexhex.casting.arithmetic.complex

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.complexhex.api.casting.iota.complex.ComplexHexIotaTypes
import net.complexhex.api.casting.iota.complex.ComplexNumberIota
import net.complexhex.stuff.ComplexNumber

// paraphrased from hexmod source lmao

object ComplexArithmetic : Arithmetic {
    val ACCEPTS_C: IotaMultiPredicate = IotaMultiPredicate.all(IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER))
    val ACCEPTS_CD: IotaMultiPredicate = IotaMultiPredicate.pair(IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER), IotaPredicate.ofType(HexIotaTypes.DOUBLE))
    val ACCEPTS_CCorD: IotaMultiPredicate = IotaMultiPredicate.either(ACCEPTS_C, ACCEPTS_CD)
    val ACCEPTS_DD: IotaMultiPredicate = IotaMultiPredicate.all(IotaPredicate.ofType(HexIotaTypes.DOUBLE))
    override fun arithName() = "complex_maths"
    private val CMUL: HexPattern = HexPattern.fromAngles("", HexDir.EAST)
    private val ARG: HexPattern = HexPattern.fromAngles("", HexDir.EAST)
    private val REAL: HexPattern = HexPattern.fromAngles("", HexDir.EAST)
    private val IMAG: HexPattern = HexPattern.fromAngles("", HexDir.EAST)
    private val CONJ: HexPattern = HexPattern.fromAngles("", HexDir.EAST)
    val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV,
        CMUL,
        ARG,
        REAL,
        IMAG,
        CONJ,
//        PACK,
//        UNPACK
    )

    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern): Operator {
        return when (pattern) {
            ADD -> CDorCbinaryC({a, b -> a.add(b)}, {a, b -> a.add(b)})
            SUB -> CDorCbinaryC({a, b -> a.sub(b)}, {a, b -> a.sub(b)})
            MUL -> CDbinaryC {a, b -> a.mul(b)}
            DIV -> CDbinaryC {a, b -> a.scalarDiv(b)}
            CMUL -> CCbinaryC {a, b -> a.mul(b)}
            ARG -> CunaryD {a -> a.argument()}
            REAL -> CunaryD {a -> a.real}
            IMAG -> CunaryD {a -> a.imag}
            CONJ -> CunaryC {a -> a.conjugate()}
            else -> throw InvalidOperatorException("$pattern is not a valid operator in complex arithmatic")
        }
    }

    fun CunaryC(op: (ComplexNumber) -> (ComplexNumber)) = OperatorUnary(ACCEPTS_C)
        {i: Iota -> ComplexNumberIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}
    fun CunaryD(op: (ComplexNumber) -> (Double)) = OperatorUnary(ACCEPTS_C)
    {i: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}

    fun CCbinaryC(op: (ComplexNumber, ComplexNumber) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_C)
        {i: Iota, j: Iota -> ComplexNumberIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}
    fun CCbinaryD(op: (ComplexNumber, ComplexNumber) -> (Double)) = OperatorBinary(ACCEPTS_C)
        {i: Iota, j: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}
    fun CDbinaryC(op: (ComplexNumber, Double) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_CD)
        {i: Iota, j: Iota -> ComplexNumberIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, HexIotaTypes.DOUBLE).double))}
//    fun pack(op: (Double, Double) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_DD)
//    {i: Iota, j: Iota -> ComplexNumberIota(op(Operator.downcast(i, HexIotaTypes.DOUBLE).double, Operator.downcast(j, HexIotaTypes.DOUBLE).double))}
//    fun unpack(op: (ComplexNumber) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_DD)
//    {i: Iota, j: Iota -> ComplexNumberIota(op(Operator.downcast(i, HexIotaTypes.DOUBLE).double, Operator.downcast(j, HexIotaTypes.DOUBLE).double))}
    // what the fuck is this
    fun CDorCbinaryC(opA:(ComplexNumber, ComplexNumber) -> (ComplexNumber), opB:(ComplexNumber, Double) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_CCorD)
        {i: Iota, j:Iota -> if (j is ComplexNumberIota) {
            ComplexNumberIota(opA(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, ComplexHexIotaTypes.COMPLEXNUMBER).complex))
        } else if (j is DoubleIota) {
            ComplexNumberIota(opB(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, HexIotaTypes.DOUBLE).double))
        } else {
            throw InvalidOperatorException("i did an oopsie, report this pls :) (${j::class})")
        }
    }
}