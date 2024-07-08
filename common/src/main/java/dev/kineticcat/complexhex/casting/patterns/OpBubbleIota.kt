package dev.kineticcat.complexhex.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kineticcat.complexhex.api.asActionResult
import dev.kineticcat.complexhex.api.casting.iota.BubbleIota
import dev.kineticcat.complexhex.stuff.Quaternion
import ram.talia.moreiotas.api.getMatrix
import kotlin.math.sqrt

object OpBubbleIota : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val iota = args[0]
        return listOf(BubbleIota(iota))
    }
}