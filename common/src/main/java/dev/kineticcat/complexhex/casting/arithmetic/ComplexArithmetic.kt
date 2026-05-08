package dev.kineticcat.complexhex.casting.arithmetic

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
import dev.kineticcat.complexhex.api.CNpow
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ComplexNumberIota
import dev.kineticcat.complexhex.stuff.ComplexNumber

// paraphrased from hexmod source lmao

object ComplexArithmetic : Arithmetic {
    private val C = IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER)
    private val D = IotaPredicate.ofType(HexIotaTypes.DOUBLE)
    private val ACCEPTS_C: IotaMultiPredicate = IotaMultiPredicate.all(C)
//    private val ACCEPTS_CC: IotaMultiPredicate = IotaMultiPredicate.pair(C, C)
//    private val ACCEPTS_CD: IotaMultiPredicate = IotaMultiPredicate.either(
//        IotaMultiPredicate.pair(C, D),
//        IotaMultiPredicate.pair(D, C)
//    )
    private val ACCEPTS_CCorCD: IotaMultiPredicate = IotaMultiPredicate.any(C, D)
    override fun arithName() = "complex_maths"
    private val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV,
        ABS,
        POW
    )

    override fun opTypes() = OPS
    override fun getOperator(pattern: HexPattern): Operator {
        return when (pattern) {
            ADD        -> CDorCCbinaryC({ a, b -> a.add(b) }, { a, b -> a.add(b) }, {a, b -> b.add(a)})
            SUB        -> CDorCCbinaryC({ a, b -> a.sub(b) }, {a, b -> a.sub(b)}, {a, b -> b.sub(a)})
            MUL        -> CDorCCbinaryC({ a, b -> a.mul(b) }, {a, b -> a.mul(b)}, {a, b -> b.mul(a)})
            DIV        -> CDbinaryC    { a, b -> a.scalarDiv(b) }
            ABS        -> CunaryD      { a -> a.modulus() }
            POW        -> CDorCCbinaryC({a, b -> a.pow(b)}, {a, b->a.pow(b)}, {a, b -> CNpow(a,b)})
            else -> throw InvalidOperatorException("$pattern is not a valid operator in complex arithmetic")
        }
    }

    fun CunaryD(op: (ComplexNumber) -> (Double)) = OperatorUnary(ACCEPTS_C)
    {i: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}

    private fun CD(cn: Iota, double: Iota, op: (ComplexNumber, Double) -> (ComplexNumber)): ComplexNumberIota {
        return ComplexNumberIota(
            op(
                Operator.downcast(cn, ComplexHexIotaTypes.COMPLEXNUMBER).complex,
                Operator.downcast(double, HexIotaTypes.DOUBLE).double
            )
        )
    }
    private fun DC(double: Iota, cn: Iota, op: (Double, ComplexNumber) -> (ComplexNumber)): ComplexNumberIota {
        return ComplexNumberIota(
            op(
                Operator.downcast(double, HexIotaTypes.DOUBLE).double,
                Operator.downcast(cn, ComplexHexIotaTypes.COMPLEXNUMBER).complex
            )
        )
    }

    private fun CC(cn1: Iota, cn2: Iota, op: (ComplexNumber, ComplexNumber) -> (ComplexNumber)): ComplexNumberIota {
        return ComplexNumberIota(
            op(
                Operator.downcast(cn1, ComplexHexIotaTypes.COMPLEXNUMBER).complex,
                Operator.downcast(cn2, ComplexHexIotaTypes.COMPLEXNUMBER).complex
            )
        )
    }

    private fun CDbinaryC(op: (ComplexNumber, Double) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_CCorCD)
        { i: Iota, j: Iota -> if (i is DoubleIota && j is ComplexNumberIota) {
            CD(j, i, op)
        } else if (i is ComplexNumberIota && j is DoubleIota) {
            CD(i, j, op)
        } else {
            throw InvalidOperatorException("i did an oopsie, report this pls :) (${i::class}, ${j::class})")
        }
    }
    // what the fuck is this
    fun CDorCCbinaryC(opA:(ComplexNumber, ComplexNumber) -> (ComplexNumber), opB:(ComplexNumber, Double) -> (ComplexNumber), opC: (Double, ComplexNumber)->(ComplexNumber)) = OperatorBinary(
        ACCEPTS_CCorCD
    )
        {i: Iota, j:Iota -> if (i is ComplexNumberIota && j is ComplexNumberIota) {
            CC(i, j, opA)
        } else if (i is DoubleIota && j is ComplexNumberIota) {
            DC(i, j, opC)
        } else if (i is ComplexNumberIota && j is DoubleIota) {
            CD(i, j, opB)
        } else {
            throw InvalidOperatorException("i did an oopsie, report this pls :) (${j::class})")
        }
    }

    fun DCbinaryC(op:(Double, ComplexNumber) -> (ComplexNumber)) = OperatorBinary(IotaMultiPredicate.pair(IotaPredicate.ofType(HexIotaTypes.DOUBLE), IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER)))
        { i:Iota, j:Iota -> ComplexNumberIota(op(Operator.downcast(i, HexIotaTypes.DOUBLE).double, Operator.downcast(j, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}
}