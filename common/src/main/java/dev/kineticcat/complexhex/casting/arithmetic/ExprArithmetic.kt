package dev.kineticcat.complexhex.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ExprIota
import dev.kineticcat.complexhex.api.util.*
import dev.kineticcat.complexhex.casting.arithmetic.ComplexHexArithmetic.*
import kotlin.math.*

object ExprArithmetic : Arithmetic {
    val E = IotaPredicate.ofType(ComplexHexIotaTypes.EXPR)
    val D = IotaPredicate.ofType(HexIotaTypes.DOUBLE)
    val ACCEPTS_E = IotaMultiPredicate.all(E)
    val ACCEPTS_E_D = IotaMultiPredicate.either(IotaMultiPredicate.all(E), IotaMultiPredicate.all(D))
    val ACCEPTS_EE = IotaMultiPredicate.pair(E, E)
    val ACCEPTS_ED = IotaMultiPredicate.pair(E, D)
    val ACCEPTS_EE_ED = IotaMultiPredicate.either(ACCEPTS_EE, ACCEPTS_ED)
    val ACCEPTS_EEE = IotaMultiPredicate.triple(E, E, E)
    override fun arithName() = "expr"
    val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV,
        POW,
        ABS,
        FLOOR,
        CEIL,
        SIN,
        COS,
        TAN,
        ARCSIN,
        ARCCOS,
        ARCTAN,
        ARCTAN2,
        LOG,
        MOD,
        AND,
        OR,
        XOR,
        GREATER,
        GREATER_EQ,
        LESS,
        LESS_EQ,
        NOT,
        PACK,
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
            ADD -> BinaryEE_ED(::Add)
            SUB -> BinaryEE_ED(::Sub)
            MUL -> BinaryEE_ED(::Mul)
            DIV -> BinaryEE_ED(::Div)
            POW -> BinaryEE_ED(::Pow)
            ABS -> UnaryE(::Abs)
            FLOOR -> UnaryE(::Floor)
            CEIL -> UnaryE(::Ceiling)
            SIN -> UnaryE(::Sin)
            COS -> UnaryE(::Cos)
            TAN -> UnaryE(::Tan)
            ARCSIN -> UnaryE(::ArcSin)
            ARCCOS -> UnaryE(::ArcCos)
            ARCTAN -> UnaryE(::ArcTan)
            ARCTAN2 -> BinaryEE_ED(::ArcTan2)
            LOG -> BinaryEE_ED(::Log)
            MOD -> BinaryEE_ED(::Modulo)
            AND -> BinaryEE_ED(::And)
            OR -> BinaryEE_ED(::Or)
            XOR -> BinaryEE_ED(::Xor)
            GREATER -> BinaryEE_ED(::GreaterThan)
            GREATER_EQ -> BinaryEE_ED(::GreaterThanOrEq)
            LESS -> BinaryEE_ED(::LessThan)
            LESS_EQ -> BinaryEE_ED(::LessThanOrEq)
            NOT -> UnaryE(::Not)
            PACK -> TernaryEEE(::Vector)
            SINH -> UnaryE_D(::Sinh, ::sinh)
            COSH -> UnaryE_D(::Cosh, ::cosh)
            TANH -> UnaryE_D(::Tanh, ::tanh)
            ASINH -> UnaryE_D(::ArcSinh, ::asinh)
            ACOSH -> UnaryE_D(::ArcCosh, ::acosh)
            ATANH -> UnaryE_D(::ArcTanh, ::atanh)
            else -> throw InvalidOperatorException("$pattern is not a valid operator for expr arith!")
        }
    }

    fun UnaryE(op: (Expr) -> (Expr)) = OperatorUnary(ACCEPTS_E) { i:Iota ->
        op(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr()).asIota()
    }
    fun UnaryE_D(opA: (Expr) -> (Expr), opB: (Double) -> Double) = OperatorUnary(ACCEPTS_E_D) { i:Iota ->
        if (i is ExprIota) {
            opA(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr()).asIota()
        } else {
            DoubleIota(opB(Operator.downcast(i, HexIotaTypes.DOUBLE).double))
        }
    }
    fun BinaryEE_ED(opA: (Expr, Expr) -> (Expr)) = OperatorBinary(ACCEPTS_EE_ED) { i:Iota, j:Iota ->
        if (j is ExprIota) {
            opA(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(j, ComplexHexIotaTypes.EXPR).expr()).asIota()
        } else {
            opA(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Value(Operator.downcast(j, HexIotaTypes.DOUBLE).double)).asIota()
        }
    }
    fun TernaryEEE(op: (Expr, Expr, Expr) -> Expr) = OperatorTerenary(ACCEPTS_EEE) {i:Iota, j:Iota, k:Iota ->
        op(Operator.downcast(i, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(j, ComplexHexIotaTypes.EXPR).expr(), Operator.downcast(k, ComplexHexIotaTypes.EXPR).expr()).asIota()
    }
}


class OperatorTerenary(accepts: IotaMultiPredicate?, var inner: (Iota, Iota, Iota) -> Iota) :
    OperatorBasic(3, accepts!!) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator()
        return listOf(inner(it.next(), it.next(), it.next()))
    }
}
