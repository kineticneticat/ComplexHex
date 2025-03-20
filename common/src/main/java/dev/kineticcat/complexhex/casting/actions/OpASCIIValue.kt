package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kineticcat.complexhex.stuff.BufferScrunge
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.api.getString
import kotlin.math.roundToInt

object OpASCIIValue : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return when (args[0]) {
            is StringIota -> {
                val string = args.getString(0, argc)
                if (string.length != 1) throw MishapInvalidIota.of(args[0], 0, "not_char")
                BufferScrunge.scrungebuf(Charsets.UTF_32.encode(string)).asActionResult
            }
            is DoubleIota -> {
                val num = args.getDouble(0, argc).roundToInt()
                Charsets.UTF_32.decode(BufferScrunge.unscrungebuf(num))[0].toString().asActionResult
            }
            else -> throw MishapInvalidIota.of(args[0], 0, "char_or_num")
        }
    }
}