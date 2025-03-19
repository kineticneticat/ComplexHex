package dev.kineticcat.complexhex.casting.actions.mathematics.longs

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.asActionResult
import dev.kineticcat.complexhex.api.getLong

object OpArithmeticRightBitshift : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return args.getLong(0, argc).shr(args.getPositiveInt(1, argc)).asActionResult
    }
}