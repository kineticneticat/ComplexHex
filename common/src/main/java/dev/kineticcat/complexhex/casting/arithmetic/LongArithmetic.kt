package dev.kineticcat.complexhex.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.LongIota

object LongArithmetic : Arithmetic {
    private val L = IotaPredicate.ofType(ComplexHexIotaTypes.LONG)
    private val ACCEPTS_L = IotaMultiPredicate.all(L);
    override fun arithName() = "long"

    private val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV
    )
    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        return when (pattern) {
            ADD -> LLbinaryL {a, b -> a+b}
            ADD -> LLbinaryL {a, b -> a-b}
            ADD -> LLbinaryL {a, b -> a*b}
            ADD -> LLbinaryL {a, b -> a/b}
            else -> throw InvalidOperatorException("$pattern is not a valid operator in complex arithmetic")
        }
    }

    private fun LLbinaryL(op: (Long, Long)-> (Long)) = OperatorBinary(ACCEPTS_L) { i:Iota, j:Iota ->
        LongIota(
            op(
                Operator.downcast(i, ComplexHexIotaTypes.LONG).long,
                Operator.downcast(j, ComplexHexIotaTypes.LONG).long
            )
        )
    }
}