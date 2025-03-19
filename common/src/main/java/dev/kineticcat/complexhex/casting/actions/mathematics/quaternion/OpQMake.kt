package dev.kineticcat.complexhex.casting.actions.mathematics.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.asActionResult
import dev.kineticcat.complexhex.stuff.Quaternion

object OpQMake : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val D = args.getDouble(0, argc)
        val V = args.getVec3(1, argc)
        return Quaternion(D, V.x, V.y, V.z).asActionResult
    }
}