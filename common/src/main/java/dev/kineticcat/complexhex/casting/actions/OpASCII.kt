package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.getString
import kotlin.math.roundToInt

class OpASCII(val toASCII: Boolean) : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (toASCII) {
            val string = args.getString(0, argc)
            if (string.length != 1) throw MishapInvalidIota.of(args[0], 0, "not_char")
            return (string.toCharArray()[0].code).asActionResult
        } else {
            val num = args.getDouble(0, argc).roundToInt()
            return num.toChar().toString().asActionResult
        }
    }
}