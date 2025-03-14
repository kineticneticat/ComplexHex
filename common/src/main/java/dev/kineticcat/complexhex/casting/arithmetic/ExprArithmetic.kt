package dev.kineticcat.complexhex.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.ADD
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.POW
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kineticcat.complexhex.api.asIota
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import symjava.symbolic.Add
import symjava.symbolic.Expr
import symjava.symbolic.Pow

object ExprArithmetic : Arithmetic {
    val E = IotaPredicate.ofType(ComplexHexIotaTypes.EXPR)
    val D = IotaPredicate.ofType(HexIotaTypes.DOUBLE)
    val ACCEPTS_EE = IotaMultiPredicate.pair(E, E)
    val ACCEPTS_ED = IotaMultiPredicate.pair(E, D)
    override fun arithName() = "expr"
    val OPS = listOf(
        ADD,
        POW
    )
    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        return when (pattern) {
            ADD -> BinaryEE {a, b -> Add(a, b)}
            POW -> BinaryED {a, b -> Pow(a, Expr.valueOf(b))}
            else -> throw InvalidOperatorException("$pattern is not a valid operator for expr arith!")
        }
    }

    fun BinaryEE(op: (Expr, Expr) -> (Expr)) = OperatorBinary(ACCEPTS_EE) {i:Iota, j:Iota ->
        op(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(j, ComplexHexIotaTypes.EXPR).expr()).asIota()
    }
    fun BinaryED(op: (Expr, Double) -> (Expr)) = OperatorBinary(ACCEPTS_ED) {i:Iota, j:Iota ->
        op(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(j, HexIotaTypes.DOUBLE).double).asIota()
    }
}