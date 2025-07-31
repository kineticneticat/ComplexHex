package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getExpr
import dev.kineticcat.complexhex.api.getSymbol

object OpDerivative : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val expr = args.getExpr(0)
        val wrt = args.getSymbol(1)
        return expr.diff(wrt).simp().asActionResult()
    }

}