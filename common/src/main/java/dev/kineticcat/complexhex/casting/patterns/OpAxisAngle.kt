package dev.kineticcat.complexhex.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.stuff.Quaternion
import org.joml.Quaterniond
import org.joml.Quaternionf


object OpAxisAngle : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val axis = args.getVec3(0, argc)
        val angle = args.getDouble(1, argc)

        return Quaternion(axis, angle).asActionResult()

    }
}