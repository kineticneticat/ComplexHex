package dev.kineticcat.complexhex.casting.actions.mathematics.expr

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getExpr
import dev.kineticcat.complexhex.api.getExprLike
import dev.kineticcat.complexhex.api.util.Equals

object OpExprEquals : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val A = args.getExpr(0)
        val B = args.getExprLike(1)
        return Equals(A, B).asActionResult()
    }
}