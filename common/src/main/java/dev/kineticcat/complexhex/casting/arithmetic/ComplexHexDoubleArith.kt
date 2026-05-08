package dev.kineticcat.complexhex.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kineticcat.complexhex.casting.arithmetic.ComplexHexArithmetic.*
import java.util.function.DoubleUnaryOperator
import kotlin.math.*

object ComplexHexDoubleArith : Arithmetic {
    override fun arithName() = "ch_double"

    val OPS = listOf(
        SINH,
        COSH,
        TANH,
        ASINH,
        ACOSH,
        ATANH
    )

    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        return when (pattern) {
            SINH -> make1 {a -> sinh(a)}
            COSH -> make1 {a -> cosh(a) }
            TANH -> make1 {a -> tanh(a) }
            ASINH -> make1 {a -> asinh(a) }
            ACOSH -> make1 {a -> acosh(a) }
            ATANH -> make1 {a -> atanh(a)}
            else -> throw InvalidOperatorException("$pattern is not a valid operator in complex hex double arithmetic!")
        }
    }

    fun make1(op: DoubleUnaryOperator) = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(HexIotaTypes.DOUBLE)))
    { i: Iota -> DoubleIota(op.applyAsDouble(Operator.downcast(i, HexIotaTypes.DOUBLE).double)) }
}