package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getExpr
import dev.kineticcat.complexhex.api.util.Value
import dev.kineticcat.complexhex.api.util.Vector
import net.minecraft.world.phys.Vec3

object OpSimplify: ConstMediaAction {
    override val argc: Int = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val expr = args.getExpr(0).simp()
//        return if (expr is Value) expr.x.asActionResult else expr.asActionResult()
        return when {
            expr is Value -> expr.x.asActionResult
            expr is Vector && expr.isPure() -> Vec3((expr.x as Value).x, (expr.y as Value).x, (expr.z as Value).x).asActionResult
            else -> expr.asActionResult()
        }
    }
}