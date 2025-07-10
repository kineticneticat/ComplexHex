package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kineticcat.complexhex.api.util.Symbol
import ram.talia.moreiotas.api.getString

object OpNewSymbol : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        var label = args.getString(0, argc)
        if (label.length != 1) throw MishapInvalidIota.of(args[0], 0, "single_char")
        return Symbol(label).asActionResult();
    }

}