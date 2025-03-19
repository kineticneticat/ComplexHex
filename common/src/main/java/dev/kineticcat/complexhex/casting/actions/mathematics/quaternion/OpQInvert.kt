package dev.kineticcat.complexhex.casting.actions.mathematics.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.asActionResult
import dev.kineticcat.complexhex.api.getQuaternion

object OpQInvert : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return args.getQuaternion(0, argc).Qinvert().asActionResult
    }
}