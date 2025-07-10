package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getExpr
import dev.kineticcat.complexhex.api.getExprOrNum

object OpSubstitute: ConstMediaAction {
    override val argc = 3

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val expr = args.getExpr(0)
        val from = args.getExprOrNum(1)
        val to = args.getExprOrNum(2)
        return expr.subsimp(from, to).asActionResult()
    }
}