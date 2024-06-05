package dev.kineticcat.complexhex.casting.arithmetic.complex

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
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ComplexNumberIota
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry.*
import dev.kineticcat.complexhex.stuff.ComplexNumber

// paraphrased from hexmod source lmao

object ComplexArithmetic : Arithmetic {
    private val ACCEPTS_C: IotaMultiPredicate = IotaMultiPredicate.all(IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER))
    private val ACCEPTS_CD: IotaMultiPredicate = IotaMultiPredicate.pair(IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER), IotaPredicate.ofType(HexIotaTypes.DOUBLE))
    private val ACCEPTS_CCorD: IotaMultiPredicate = IotaMultiPredicate.either(ACCEPTS_C, ACCEPTS_CD)
    override fun arithName() = "complex_maths"
    private val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV,
        COMPLEXMUL,
        ABS,
        REAL,
        IMAGINARY,
        CONJUGATE,
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
            COMPLEXMUL -> CCbinaryC {a, b -> a.mul(b)}
            ABS -> CunaryD {a -> a.argument()}
            REAL -> CunaryD {a -> a.real}
            IMAGINARY -> CunaryD {a -> a.imag}
            CONJUGATE -> CunaryC {a -> a.conjugate()}
            else -> throw InvalidOperatorException("$pattern is not a valid operator in complex arithmetic")
        }
    }

    fun CunaryC(op: (ComplexNumber) -> (ComplexNumber)) = OperatorUnary(ACCEPTS_C)
        {i: Iota -> ComplexNumberIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex)) }
    fun CunaryD(op: (ComplexNumber) -> (Double)) = OperatorUnary(ACCEPTS_C)
    {i: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}

    fun CCbinaryC(op: (ComplexNumber, ComplexNumber) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_C)
        {i: Iota, j: Iota -> ComplexNumberIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, ComplexHexIotaTypes.COMPLEXNUMBER).complex)) }
    fun CCbinaryD(op: (ComplexNumber, ComplexNumber) -> (Double)) = OperatorBinary(ACCEPTS_C)
        {i: Iota, j: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}
    private fun CDbinaryC(op: (ComplexNumber, Double) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_CD)
        {i: Iota, j: Iota -> ComplexNumberIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, HexIotaTypes.DOUBLE).double)) }
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