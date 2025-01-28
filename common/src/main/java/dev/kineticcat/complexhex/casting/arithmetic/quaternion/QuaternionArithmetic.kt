package dev.kineticcat.complexhex.casting.arithmetic.quaternion

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
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry.*
import dev.kineticcat.complexhex.stuff.Quaternion
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

object QuaternionArithmetic : Arithmetic {

    private val Q_PREDICATE = IotaPredicate.ofType(ComplexHexIotaTypes.QUATERNION)
    private val D_PREDICATE = IotaPredicate.ofType(HexIotaTypes.DOUBLE)
    private val V_PREDICATE = IotaPredicate.ofType(HexIotaTypes.VEC3)
    val ACCEPTS_Q: IotaMultiPredicate = IotaMultiPredicate.all(Q_PREDICATE)
    private val ACCEPTS_QD: IotaMultiPredicate = IotaMultiPredicate.either(
        IotaMultiPredicate.pair(IotaPredicate.ofType(ComplexHexIotaTypes.QUATERNION), IotaPredicate.ofType(HexIotaTypes.DOUBLE)),
        IotaMultiPredicate.pair(IotaPredicate.ofType(HexIotaTypes.DOUBLE), IotaPredicate.ofType(ComplexHexIotaTypes.QUATERNION))
    )
    private val ACCEPTS_QQorQD: IotaMultiPredicate = IotaMultiPredicate.either(ACCEPTS_Q, ACCEPTS_QD)
    private val ACCEPTS_DV: IotaMultiPredicate = IotaMultiPredicate.pair(D_PREDICATE, V_PREDICATE)
    override fun arithName() = "quaternion_maths"
    private val OPS = listOf(
            ADD,
            SUB,
            MUL,
            DIV,
            ABS,
            QINVERT,
            QW,
            QX,
            QY,
            QZ,
            QMAKE,
            QUNMAKE
    )
    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        val out =  when (pattern) {
            ADD     -> QDorQQbinaryQ( { a, b -> a.Qadd(b) }, { a, b -> a.Qadd(b) })
            SUB     -> QDorQQbinaryQ( { a, b -> a.Qsub(b) }, { a, b -> a.Qsub(b) })
            MUL     -> QDorQQbinaryQ( { a, b -> a.Qmul(b) }, { a, b -> a.Qmul(b) })
            DIV     -> QDbinaryQ      { a, b -> a.Qdiv(b) }
            ABS     -> QunaryD        { a    -> sqrt(a.lengthSquared()) }
            QINVERT -> QunaryQ        { a    -> a.Qinvert() }
            QW      -> QunaryD        { a    -> a.w }
            QX      -> QunaryD        { a    -> a.x }
            QY      -> QunaryD        { a    -> a.y }
            QZ      -> QunaryD        { a    -> a.z }
            QMAKE   -> DVbinaryQ      { d, v -> Quaternion(d, v.x, v.y, v.z) }
            QUNMAKE -> OpQunmake
            else -> throw InvalidOperatorException("$pattern is not a valid operator in quaternion arithmetic")
        }
        return out
    }

    fun QunaryQ(op: (Quaternion) -> (Quaternion)) = OperatorUnary(ACCEPTS_Q)
    {i: Iota -> QuaternionIota(op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion)) }
    fun QunaryD(op: (Quaternion) -> (Double)) = OperatorUnary(ACCEPTS_Q)
    {i: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion))}

    private fun QD(cn: Iota, double: Iota, op: (Quaternion, Double) -> (Quaternion)): QuaternionIota {
        return QuaternionIota(
            op(
                Operator.downcast(cn, ComplexHexIotaTypes.QUATERNION).quaternion,
                Operator.downcast(double, HexIotaTypes.DOUBLE).double
            )
        )
    }
    private fun QQ(cn1: Iota, cn2: Iota, op: (Quaternion, Quaternion) -> (Quaternion)): QuaternionIota {
        return QuaternionIota(
            op(
                Operator.downcast(cn1, ComplexHexIotaTypes.QUATERNION).quaternion,
                Operator.downcast(cn2, ComplexHexIotaTypes.QUATERNION).quaternion
            )
        )
    }

    private fun QDbinaryQ(op: (Quaternion, Double) -> (Quaternion)) = OperatorBinary(ACCEPTS_QD)
    { i: Iota, j: Iota -> if (i is DoubleIota && j is QuaternionIota) {
        QD(j, i, op)
    } else if (i is QuaternionIota && j is DoubleIota) {
        QD(i, j, op)
    } else {
        throw InvalidOperatorException("i did an oopsie, report this pls :) (${i::class}, ${j::class})")
    }
    }
    // what the fuck is this
    fun QDorQQbinaryQ(opA:(Quaternion, Quaternion) -> (Quaternion), opB:(Quaternion, Double) -> (Quaternion)) = OperatorBinary(
        ACCEPTS_QQorQD
    )
    {i: Iota, j:Iota -> if (i is QuaternionIota && j is QuaternionIota) {
        QQ(i, j, opA)
    } else if (i is DoubleIota && j is QuaternionIota) {
        QD(j, i, opB)
    } else if (i is QuaternionIota && j is DoubleIota) {
        QD(i, j, opB)
    } else {
        throw InvalidOperatorException("i did an oopsie, report this pls :) (${j::class})")
    }
    }
    private fun DVbinaryQ(op: (Double, Vec3) -> Quaternion) = OperatorBinary(ACCEPTS_DV)
        {i: Iota, j:Iota -> op(Operator.downcast(i, HexIotaTypes.DOUBLE).double, Operator.downcast(j, HexIotaTypes.VEC3).vec3).asIota()}

}