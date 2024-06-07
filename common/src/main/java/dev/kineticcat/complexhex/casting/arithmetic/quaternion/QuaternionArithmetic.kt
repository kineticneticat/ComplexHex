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
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry.*
import dev.kineticcat.complexhex.stuff.Quaternion
import net.minecraft.world.phys.Vec3
import org.apache.logging.log4j.LogManager

object QuaternionArithmetic : Arithmetic {
    val LOGGER = LogManager.getLogger(Complexhex.MOD_ID)

    private val Q_PREDICATE = IotaPredicate.ofType(ComplexHexIotaTypes.QUATERNION)
    private val D_PREDICATE = IotaPredicate.ofType(HexIotaTypes.DOUBLE)
    private val V_PREDICATE = IotaPredicate.ofType(HexIotaTypes.VEC3)
    public val ACCEPTS_Q: IotaMultiPredicate = IotaMultiPredicate.all(Q_PREDICATE)
    private val ACCEPTS_QD: IotaMultiPredicate = IotaMultiPredicate.pair(Q_PREDICATE, D_PREDICATE)
    private val ACCEPTS_DQ: IotaMultiPredicate = IotaMultiPredicate.pair(D_PREDICATE, Q_PREDICATE)
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
            QA,
            QB,
            QC,
            QD,
            QMAKE,
            QUNMAKE
    )
    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        val out =  when (pattern) {
            ADD     -> QQorQDbinaryQ( { a, b -> a.add(b) }, { a, b -> a.add(b) } )
            SUB     -> QQorQDbinaryQ( { a, b -> a.sub(b) }, { a, b -> a.sub(b) } )
            MUL     -> QDbinaryQ      { a, b -> a.mul(b) }
            DIV     -> QDbinaryQ      { a, b -> a.div(b) }
            ABS     -> QunaryD        { a    -> a.length() }
            QMUL    -> QQbinaryQ      { a, b -> a.mul(b) }
            QINVERT -> QunaryQ        { a    -> a.inverse() }
            QA      -> QunaryD        { a    -> a.a }
            QB      -> QunaryD        { a    -> a.b }
            QC      -> QunaryD        { a    -> a.c }
            QD      -> QunaryD        { a    -> a.d }
            QMAKE   -> DVbinaryQ      { d, v -> Quaternion(d, v.x, v.y, v.z) }
            QUNMAKE -> OpQunmake
            else -> throw InvalidOperatorException("$pattern is not a valid operator in quaternion arithmetic")
        }
        return out
    }

    fun QunaryQ(op: (Quaternion) -> (Quaternion)) = OperatorUnary(ACCEPTS_Q)
        {i: Iota -> op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion).asIota() }
    fun QunaryD(op: (Quaternion) -> (Double)) = OperatorUnary(ACCEPTS_Q)
        {i: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion))}
    fun QQbinaryQ(op: (Quaternion, Quaternion) -> (Quaternion)) = OperatorBinary(ACCEPTS_Q)
        {i: Iota, j: Iota -> op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion, Operator.downcast(j, ComplexHexIotaTypes.QUATERNION).quaternion).asIota() }
    private fun QDbinaryQ(op: (Quaternion, Double) -> (Quaternion)) = OperatorBinary(ACCEPTS_QD)
        {i: Iota, j: Iota -> op(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion, Operator.downcast(j, HexIotaTypes.DOUBLE).double).asIota() }
    private fun QQorQDbinaryQ(opA:(Quaternion, Quaternion) -> (Quaternion), opB:(Quaternion, Double) -> (Quaternion)) = OperatorBinary(ACCEPTS_QQorQD)
    {i: Iota, j:Iota -> if (j is QuaternionIota) {
            opA(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion, Operator.downcast(j, ComplexHexIotaTypes.QUATERNION).quaternion).asIota()
        } else if (j is DoubleIota) {
            opB(Operator.downcast(i, ComplexHexIotaTypes.QUATERNION).quaternion, Operator.downcast(j, HexIotaTypes.DOUBLE).double).asIota()
        } else {
            throw InvalidOperatorException("i did an oopsie, report this pls :3 (${j::class})")
        }
    }
    private fun DVbinaryQ(op: (Double, Vec3) -> Quaternion) = OperatorBinary(ACCEPTS_DV)
        {i: Iota, j:Iota -> op(Operator.downcast(i, HexIotaTypes.DOUBLE).double, Operator.downcast(j, HexIotaTypes.VEC3).vec3).asIota()}

}