package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getExpr
import dev.kineticcat.complexhex.api.getExprOrNum
import dev.kineticcat.complexhex.api.util.Piecewise

object OpPiecewise : ConstMediaAction {
    override val argc = 3

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val condition = args.getExpr(0)
        val ifTrue = args.getExprOrNum(1)
        val ifFalse = args.getExprOrNum(2)
        return Piecewise(condition, ifTrue, ifFalse).asActionResult()
    }
}