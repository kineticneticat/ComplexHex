package dev.kineticcat.complexhex.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kineticcat.complexhex.api.asIota
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ExprIota
import symjava.symbolic.Abs
import symjava.symbolic.Expr
import symjava.symbolic.Pow

object ExprArithmetic : Arithmetic {
    val E = IotaPredicate.ofType(ComplexHexIotaTypes.EXPR)
    val D = IotaPredicate.ofType(HexIotaTypes.DOUBLE)
    val ACCEPTS_E = IotaMultiPredicate.all(E)
    val ACCEPTS_EE = IotaMultiPredicate.pair(E, E)
    val ACCEPTS_ED = IotaMultiPredicate.pair(E, D)
    val ACCEPTS_EE_ED = IotaMultiPredicate.either(ACCEPTS_EE, ACCEPTS_ED)
    override fun arithName() = "expr"
    val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV,
        POW,
        ABS
    )
    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        return when (pattern) {
            ADD -> BinaryEE_ED({a, b -> a.add(b)}, {a, b -> a.add(b)})
            SUB -> BinaryEE_ED({a, b -> a.subtract(b)}, {a, b -> a.subtract(b)})
            MUL -> BinaryEE_ED({ a, b -> a.multiply(b) }, { a, b -> a.multiply(b) })
            DIV -> BinaryEE_ED({ a, b -> a.divide(b)}, { a, b -> a.divide(b)})
            POW -> BinaryEE_ED({ a, b -> Pow(a, b) }, { a, b -> Pow(a, Expr.valueOf(b)) })
            ABS -> UnaryE { a -> Abs(a) }
            else -> throw InvalidOperatorException("$pattern is not a valid operator for expr arith!")
        }
    }

    fun UnaryE(op: (Expr) -> (Expr)) = OperatorUnary(ACCEPTS_E) {i:Iota ->
        op(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr()).asIota()
    }
    fun BinaryEE_ED(opA: (Expr, Expr) -> (Expr), opB: (Expr, Double) -> (Expr)) = OperatorBinary(ACCEPTS_EE_ED) { i:Iota, j:Iota ->
        if (j is ExprIota) {
            opA(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(j, ComplexHexIotaTypes.EXPR).expr()).asIota()
        } else {
            opB(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(j, HexIotaTypes.DOUBLE).double).asIota()
        }
    }
}