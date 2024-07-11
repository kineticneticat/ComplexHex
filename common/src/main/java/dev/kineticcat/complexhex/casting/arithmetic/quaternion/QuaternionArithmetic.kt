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
    private val ACCEPTS_QD: IotaMultiPredicate = IotaMultiPredicate.pair(Q_PREDICATE, D_PREDICATE)
    private val ACCEPTS_QQorQD: IotaMultiPredicate = IotaMultiPredicate.either(ACCEPTS_Q, ACCEPTS_QD)
    private val ACCEPTS_DV: IotaMultiPredicate = IotaMultiPredicate.pair(D_PREDICATE, V_PREDICATE)
    override fun arithName() = "quaternion_maths"
    private val OPS = listOf(
            ADD,
            SUB,
            MUL,
            DIV,
            ABS,
            QMUL,
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
            ADD     -> QQbinaryQ      { a, b -> a.Qadd(b) }
            SUB     -> QQbinaryQ      { a, b -> a.Qsub(b) }
            MUL     -> QDbinaryQ      { a, b -> a.Qmul(b) }
            DIV     -> QDbinaryQ      { a, b -> a.Qdiv(b) }
            ABS     -> QunaryD        { a    -> sqrt(a.lengthSquared()) }
            QMUL    -> QQbinaryQ      { a, b -> a.Qmul(b) }
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

    private fun QunaryQ(op: (Quaternion) -> (Quaternion)) = OperatorUnary(ACCEPTS_Q)
        {i: Iota -> op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion).asIota() }
    private fun QunaryD(op: (Quaternion) -> (Double)) = OperatorUnary(ACCEPTS_Q)
        {i: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion))}
    private fun QQbinaryQ(op: (Quaternion, Quaternion) -> (Quaternion)) = OperatorBinary(ACCEPTS_Q)
        {i: Iota, j: Iota -> op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion, Operator.downcast(j, ComplexHexIotaTypes.QUATERNION).quaternion).asIota() }
    private fun QDbinaryQ(op: (Quaternion, Double) -> (Quaternion)) = OperatorBinary(ACCEPTS_QD)
        {i: Iota, j: Iota -> op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion, Operator.downcast(j, HexIotaTypes.DOUBLE).double).asIota() }
    private fun DVbinaryQ(op: (Double, Vec3) -> Quaternion) = OperatorBinary(ACCEPTS_DV)
        {i: Iota, j:Iota -> op(Operator.downcast(i, HexIotaTypes.DOUBLE).double, Operator.downcast(j, HexIotaTypes.VEC3).vec3).asIota()}

}