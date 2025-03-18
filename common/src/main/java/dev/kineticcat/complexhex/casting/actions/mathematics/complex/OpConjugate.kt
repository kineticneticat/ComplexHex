package dev.kineticcat.complexhex.casting.actions.mathematics.complex

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getComplex

object OpConjugate : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return listOf(args.getComplex(0).conjugate().asIota())
    }
}