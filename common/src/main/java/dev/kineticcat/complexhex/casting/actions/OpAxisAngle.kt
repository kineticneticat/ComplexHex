package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.util.Quaternion


object OpAxisAngle : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val axis = args.getVec3(0, argc)
        val angle = args.getDouble(1, argc)

        return Quaternion(axis, angle).asActionResult()

    }
}