package dev.kineticcat.complexhex.casting.actions.fields

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes

object OpDevPrintField : ConstMediaAction  {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val field = Operator.downcast(args.get(0), ComplexHexIotaTypes.FIELD).getField(env.world.server)
        Complexhex.LOGGER.info(field.data)
        return listOf()
    }
}