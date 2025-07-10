package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getExpr
import dev.kineticcat.complexhex.api.util.Value

object OpSimplify: ConstMediaAction {
    override val argc: Int = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        var expr = args.getExpr(0).simp()
        return if (expr is Value) expr.x.asActionResult else expr.asActionResult()
    }
}