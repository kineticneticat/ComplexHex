package dev.kineticcat.complexhex.casting.actions.mathematics.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import dev.kineticcat.complexhex.api.getQuaternion
import net.minecraft.world.phys.Vec3

object OpQUnmake : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val Q = args.getQuaternion(0, argc)
        return listOf(DoubleIota(Q.w), Vec3Iota(Vec3(Q.x, Q.y, Q.z)))
    }
}