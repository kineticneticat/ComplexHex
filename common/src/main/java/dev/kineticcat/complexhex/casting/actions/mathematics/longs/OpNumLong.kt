package dev.kineticcat.complexhex.casting.actions.mathematics.longs

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import dev.kineticcat.complexhex.api.asActionResult
import dev.kineticcat.complexhex.api.casting.iota.LongIota

class OpNumLong(val toLong: Boolean) : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return when (val num = args[0]) {
            is DoubleIota ->num.double.toLong().asActionResult
            is LongIota -> num.long.toDouble().asActionResult
            else -> throw MishapInvalidIota(num, 0, "double_or_long".asTranslatedComponent)
        }
    }
}