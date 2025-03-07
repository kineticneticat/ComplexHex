package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.asActionResult
import ram.talia.moreiotas.api.getString
import symjava.symbolic.Symbol

object OpNewSymbol : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        var label = args.getString(0, argc)
        return Symbol(label).asActionResult();
    }

}