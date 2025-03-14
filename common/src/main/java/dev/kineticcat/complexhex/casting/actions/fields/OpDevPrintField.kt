package dev.kineticcat.complexhex.casting.actions.fields

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.util.Field

object OpDevPrintField : ConstMediaAction  {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val field = Operator.downcast(args[0], ComplexHexIotaTypes.FIELD).getField(env.world.server)
//        Complexhex.LOGGER.info(field.data)
        val F = Field.Zeros(10, 10, 10, 3)
        println("original field")
        println(F.data)
        val ser = F.serialise()
        println("serialised")
        println(ser)
        val deser = Field.deserialise(ser)
        println("deserialised")
        println(deser.data)
        return listOf()
    }
}