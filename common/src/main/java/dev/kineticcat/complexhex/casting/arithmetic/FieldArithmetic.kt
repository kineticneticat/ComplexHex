package dev.kineticcat.complexhex.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidOperatorArgs
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.FieldIota
import dev.kineticcat.complexhex.api.util.Field
import dev.kineticcat.complexhex.util.DataStorage

object FieldArithmetic : Arithmetic {
    private val F = IotaPredicate.ofType(ComplexHexIotaTypes.FIELD)
    private val D = IotaPredicate.ofType(HexIotaTypes.DOUBLE)
    private val V = IotaPredicate.ofType(HexIotaTypes.VEC3)
    private val ACCEPTS_F = IotaMultiPredicate.all(F)
    private val ACCEPTS_D = IotaMultiPredicate.all(D)
    private val ACCEPTS_V = IotaMultiPredicate.all(V)
    private val ACCEPTS_FF = IotaMultiPredicate.Pair(F, F)
    private val ACCEPTS_FD = IotaMultiPredicate.Pair(F, D)
    private val ACCEPTS_FV = IotaMultiPredicate.Pair(F, V)
    private val ACCEPTS_FForFDorFV = IotaMultiPredicate.either(ACCEPTS_FF, IotaMultiPredicate.either(ACCEPTS_FD, ACCEPTS_FV))

    override fun arithName() = "field_maths"
    private val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV
    )
    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern?): Operator {
        return when (pattern) {
            ADD -> make {a, b -> a+b}
            SUB -> make {a, b -> a-b}
            MUL -> make {a, b -> a*b}
            DIV -> make {a, b -> a/b}
            else -> throw InvalidOperatorException("$pattern is not a valid operator in field arithmetic")
        }
    }

    fun make(
        op: (Double, Double) -> (Double)
    ) = OperatorBinaryEnv(ACCEPTS_FForFDorFV) { i:Iota, j:Iota, env:CastingEnvironment ->
        if (j is FieldIota) {
            if ((i as FieldIota).shape != j.shape) throw MishapInvalidOperatorArgs(listOf(i, j))
            val iName = Operator.downcast(i, ComplexHexIotaTypes.FIELD).name
            val jName = Operator.downcast(j, ComplexHexIotaTypes.FIELD).name
            val iField = Field.deserialise(DataStorage.getServerData(env.world.server).Fields[iName])
            val jField = Field.deserialise(DataStorage.getServerData(env.world.server).Fields[jName])
            iField.applyField(jField, op)
            DataStorage.setField(env.world, iName, iField)
            Complexhex.LOGGER.info(iField)
        } else if (j is DoubleIota) {
            val iName = Operator.downcast(i, ComplexHexIotaTypes.FIELD).name
            val iField = Field.deserialise(DataStorage.getServerData(env.world.server).Fields[iName])
            val jNum = Operator.downcast(j, HexIotaTypes.DOUBLE).double
            iField.applyUniform(jNum, op)
            DataStorage.setField(env.world, iName, iField)
        } else if (j is Vec3Iota) {
            // uhh ill deal with it later
            throw MishapInvalidOperatorArgs(listOf(i,j))
        }
        DataStorage.getServerData(env.world.server).setDirty()
        i
    }
}