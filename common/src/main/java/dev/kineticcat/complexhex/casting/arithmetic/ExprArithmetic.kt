package dev.kineticcat.complexhex.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.ADD
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import dev.kineticcat.complexhex.api.asIota
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import symjava.symbolic.Add
import symjava.symbolic.Expr

object ExprArithmetic : Arithmetic {
    val E = IotaPredicate.ofType(ComplexHexIotaTypes.EXPR)
    val ACCEPTS_EE = IotaMultiPredicate.pair(E, E)
    override fun arithName() = "expr"
    val OPS = listOf(
        ADD
    )
    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        return when (pattern) {
            ADD -> BinaryEE {a, b -> Add(a, b)}
            else -> throw InvalidOperatorException("$pattern is not a valid operator for expr arith!")
        }
    }

    fun BinaryEE(op: (Expr, Expr) -> (Expr)) = OperatorBinary(ACCEPTS_EE) {i:Iota, j:Iota ->
        op(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(j, ComplexHexIotaTypes.EXPR).expr()).asIota()
    }
}